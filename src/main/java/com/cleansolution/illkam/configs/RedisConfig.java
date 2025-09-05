package com.cleansolution.illkam.configs;

import com.cleansolution.illkam.chats.socket.RedisMessageSubscriber;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer( // (1)
                                                                        RedisConnectionFactory connectionFactory,
                                                                        MessageListenerAdapter listenerAdapter,
                                                                        ChannelTopic channelTopic,
                                                                        MessageListenerAdapter readListenerAdapter,
                                                                        ChannelTopic channelTopicRead
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, channelTopic);
        container.addMessageListener(readListenerAdapter, channelTopicRead);
        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(RedisMessageSubscriber subscriber) { // (2)
        return new MessageListenerAdapter(subscriber, "onMessage");
    }

    @Bean
    public MessageListenerAdapter readListenerAdapter(RedisMessageSubscriber subscriber) { // (2)
        return new MessageListenerAdapter(subscriber, "onMessageRead");
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate
            (RedisConnectionFactory connectionFactory) { // (3)
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        // Redis에서 사용할 ObjectMapper 설정 (배열 형태 날짜 저장)
        ObjectMapper redisObjectMapper = new ObjectMapper();
        redisObjectMapper.registerModule(new JavaTimeModule());
        redisObjectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Jackson을 사용한 Serializer 설정
        GenericJackson2JsonRedisSerializer serializer =
                new GenericJackson2JsonRedisSerializer(redisObjectMapper);

        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    @Bean
    public ChannelTopic channelTopic() { // (4)
        return new ChannelTopic("chatroom");
    }

    @Bean
    public ChannelTopic channelTopicRead() { // (4)
        return new ChannelTopic("read");
    }
}