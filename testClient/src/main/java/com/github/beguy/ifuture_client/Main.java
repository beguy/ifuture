package com.github.beguy.ifuture_client;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    private final static Logger log = Logger.getLogger(Main.class.getName());

    private static Integer idRangeStart = 0;
    private static Integer idRangeStop = 0;
    private static List<Integer> idList = Collections.emptyList();
    private static Integer rCount = null;
    private static Integer wCount = null;
    private static Integer writerValue = 1;
    private static URL serverHttpAddress;

    public static void main(String[] args) {
        Options options = getCliOptions();
        try {
            parseCli(args, options);
        } catch (ParseException e) {
            log.warning(() -> "Illegal commands: " + e.getMessage() + ", try to read -h --help");
            return;
        }

        if (rCount != null) {
            Runnable reader = new readerClient();
            execute(reader, rCount).shutdown();
        }

        if (wCount != null) {
            Runnable writer = new writerClient();
            execute(writer, wCount).shutdown();
        }
    }

    private static ExecutorService execute(Runnable runnable, Integer threads) {
        ExecutorService service = Executors.newFixedThreadPool(threads);
        for (int i = 0; i < threads; ++i) service.execute(runnable);
        return service;
    }

    private static void parseCli(String[] args, Options options) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        CommandLine line = parser.parse(options, args);

        if (line.hasOption("help")) {
            printHelp(options);
        }

        if (line.hasOption("rCount")) {
            String optionValue = line.getOptionValue("rCount");
            rCount = Integer.valueOf(optionValue);
            log.config(() -> "rCount: " + Integer.valueOf(optionValue));
        }

        if (line.hasOption("wCount")) {
            String optionValue = line.getOptionValue("wCount");
            wCount = Integer.valueOf(optionValue);
            log.config(() -> "wCount: " + Integer.valueOf(optionValue));
        }
        if (rCount == null && wCount == null) throw new ParseException("rCount or wCount required");

        if (line.hasOption("idList")) {
            String optionValue = line.getOptionValue("idList");
            if (Pattern.matches("\\[\\d+,\\d+\\]", optionValue)) {
                Matcher matcher = Pattern.compile("\\d+").matcher(optionValue);
                matcher.find();
                idRangeStart = Integer.valueOf(matcher.group());
                matcher.find();
                idRangeStop = Integer.valueOf(matcher.group());

                log.config(() -> "Range: " + idRangeStart.toString() + " " + idRangeStop.toString());
            } else if (Pattern.matches("(\\d|\\d\\s*,\\s*)+", optionValue)) {
                idList = Pattern.compile("\\d")
                        .matcher(optionValue)
                        .results()
                        .map(MatchResult::group)
                        .map(Integer::valueOf)
                        .collect(Collectors.toList());
                if (idList.get(0).equals(idList.get(1))) throw new ParseException("equal idRange");
                log.config(() ->
                        "Ids: " + idList.stream()
                                .map(i -> i.toString())
                                .collect(Collectors.joining(", "))
                );
            } else {
                throw new ParseException("wrong value in idList");
            }
        } else throw new ParseException("idList required");
        try {
            serverHttpAddress = new URL(args[args.length - 1]);
            log.config(() -> "target address: " + serverHttpAddress.toString());
        } catch (MalformedURLException e) {
            ParseException exc = new ParseException("illegal address");
            exc.addSuppressed(e);
            throw exc;
        }
    }

    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("testClient [options] [server http target with %id]", options);
    }

    private static Options getCliOptions() {
        Options options = new Options();
        options.addOption(Option.builder("rN")
                .longOpt("rCount")
                .hasArg().type(int.class)
                .desc("Number of readers requesting the getAmount(id) method").build());
        options.addOption(Option.builder("wN")
                .longOpt("wCount")
                .hasArg().type(int.class)
                .desc("Number of readers requesting the addAmount(id,value) method").build());
        options.addOption(Option.builder("id")
                .longOpt("idList")
                .hasArg().type(String.class).argName("idRange like: \"[1,3]\" or list ids: \"1,2,3,4\"")
                .desc("List or idRange of keys that will be used for testing").build());
        options.addOption(Option.builder("f")
                .longOpt("propertyFile")
                .hasArg().type(String.class)
                .desc("List or idRange of keys that will be used for testing").build());
        options.addOption(Option.builder("h")
                .longOpt("help")
                .desc("This help").build());
        return options;
    }

    private static class readerClient implements Runnable {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        private void doGetAmount(Integer id) {
            HttpGet request = new HttpGet(serverHttpAddress.toString().replaceFirst("%id", id.toString()));
            try (CloseableHttpResponse response1 = httpClient.execute(request)) {
                HttpEntity entity = response1.getEntity();
                // Ensure it is fully consumed
                EntityUtils.consume(entity);
            } catch (IOException e) {
                log.warning("reader error: " + e);
            }
        }

        @Override
        public void run() {
            idList.forEach(this::doGetAmount);
            IntStream.range(idRangeStart, ++idRangeStop).forEach(this::doGetAmount);
        }
    }

    private static class writerClient implements Runnable {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        private void doPostAmount(Integer id, Integer value) {
            HttpPost httpPost = new HttpPost(serverHttpAddress.toString().replaceFirst("%id", id.toString()));
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("value", value.toString()));
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try (CloseableHttpResponse response2 = httpClient.execute(httpPost)) {
                HttpEntity entity = response2.getEntity();
                // Ensure it is fully consumed
                EntityUtils.consume(entity);
            } catch (IOException e) {
                log.warning("writer error: " + e);
            }
        }

        @Override
        public void run() {
            idList.forEach(id -> doPostAmount(id, writerValue));
            IntStream.range(idRangeStart, ++idRangeStop).forEach(id -> doPostAmount(id, writerValue));
        }
    }
}
