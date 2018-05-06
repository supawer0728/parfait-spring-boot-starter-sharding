package com.parfait.study.starter.sharding.selector;

public interface ShardKeyGettable {

    String getShardKey(Object object);
}
