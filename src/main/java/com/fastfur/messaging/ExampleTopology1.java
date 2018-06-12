package com.fastfur.messaging;

import com.fastfur.messaging.data.Tweet;
import com.fastfur.messaging.producer.TwittProducer;
import com.fastfur.messaging.serde.TweetSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.Consumed;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;

/**
 * Created by dimitryw on 6/12/18.
 */
public class ExampleTopology1{

    public static final String INPUT_TOPIC_NAME = "twitters";
    public static final String QUERY = "q=@realDonaldTrump";

    public static void main(String[] args) throws Exception {
        TwittProducer tp = new TwittProducer();
        tp.produceTweets(INPUT_TOPIC_NAME, QUERY);
        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "my-first-tweet-ks");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.70.150:9092");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, TweetSerde.class);


        StreamsBuilder builder = new StreamsBuilder();
        KStream<String,Tweet> stream = builder.stream(INPUT_TOPIC_NAME, Consumed.with(Serdes.String(), new TweetSerde()));
        stream.foreach((k,v)->System.out.println(v.toString()));
        KafkaStreams streams = new KafkaStreams(builder.build(),config);
        streams.start();
    }

}
