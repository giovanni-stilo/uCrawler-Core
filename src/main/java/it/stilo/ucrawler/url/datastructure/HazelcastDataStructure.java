package it.stilo.ucrawler.url.datastructure;

/*
 * #%L
 * uCrawler
 * %%
 * Copyright (C) 2012 - 2018 Giovanni Stilo
 * %%
 * uCrawler is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program.  If not, see
 * <https://www.gnu.org/licenses/lgpl-3.0.txt>.
 * #L%
 */

import it.stilo.ucrawler.page.TinyCrawlable;
import com.hazelcast.core.DistributedObject;
import com.hazelcast.core.DistributedObjectEvent;
import com.hazelcast.core.DistributedObjectListener;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.Member;
import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;
import it.stilo.ucrawler.page.Page;
import java.net.URISyntaxException;
import java.util.Queue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.PriorityBlockingQueue;
import org.apache.log4j.Logger;

/**
 *
 * @author stilo & metelli & musolino
 */
public class HazelcastDataStructure implements URLManagerDataStructureIF, DistributedObjectListener, MessageListener<Object> {

    private ConcurrentMap<String, String> urlMap;
    private Queue<TinyCrawlable> localQueue;
    private Queue<String> topicsQueue; // distributed queue of topics 
    private HazelcastInstance instance;
    private String UUID; // local identifier

    public HazelcastDataStructure(HazelcastInstance instance) {
        this.instance = instance;
        this.instance.addDistributedObjectListener(this);

        this.urlMap = instance.getMap("csi.uri.map");

        UUID = this.instance.getCluster().getLocalMember().getUuid();
        instance.getTopic(UUID).addMessageListener(this);
        this.topicsQueue = instance.getQueue("csi.topics.queue");
        this.topicsQueue.add(UUID);

        this.localQueue = new PriorityBlockingQueue<TinyCrawlable>(100, TinyCrawlable.getBFSComparator()); // BFS Crawling

        Logger.getLogger(this.getClass()).info("UUID USER: " + UUID);
        Logger.getLogger(this.getClass()).info("URL-MAP SIZE: " + urlMap.size());
        Logger.getLogger(this.getClass()).info("TOPIC-QUEUE SIZE: " + topicsQueue.size());
    }

    @Override
    public synchronized boolean isInMap(Page p) {
        return urlMap.containsKey(p.getUri().toString());
    }

    @Override
    public synchronized boolean isInQueue(Page p) {
        try {
            return localQueue.contains(new TinyCrawlable(p));
        } catch (URISyntaxException ex) {
            Logger.getLogger(this.getClass()).debug(p.getUri(), ex);
        }
        return false;
    }

    @Override
    public synchronized void add(Page p) {
        urlMap.putIfAbsent(p.getUri().toString(), "1");

       try {
            assignToClient(p);
        } catch (URISyntaxException ex) {
            Logger.getLogger(this.getClass()).debug(p.getUri(), ex);
        }
    }

    private void assignToClient(Page page) throws URISyntaxException {

        boolean published = false;

        while (!published) {
            String topicID = topicsQueue.poll();
            if (topicID != null) {
                for (Member m : this.instance.getCluster().getMembers()) {
                    if (m.getUuid().equals(topicID)) {
                        this.instance.getTopic(topicID).publish(new TinyCrawlable(page));
                        topicsQueue.offer(topicID);
                        Logger.getLogger(this.getClass()).info("TOPIC-QUEUE SIZE: " + topicsQueue.size());
                        published = true;
                    }
                }
            } else {
                this.localQueue.add(new TinyCrawlable(page));
                published = true;
            }
        }
    }

    @Override
    public int getSize() {
        return this.localQueue.size();
    }

    @Override
    public synchronized Page getNext() {
        TinyCrawlable next;
        if (!localQueue.isEmpty() && (next = localQueue.poll()) != null) {
            return next.createFullPage();
        }
        return null;
    }

    @Override
    public void distributedObjectCreated(DistributedObjectEvent event) {

        DistributedObject dist = event.getDistributedObject();
        Logger.getLogger(this.getClass()).info("Created: " + dist.getName() + "," + dist.getId());
    }

    @Override
    public void distributedObjectDestroyed(DistributedObjectEvent event) {

        DistributedObject dist = event.getDistributedObject();
        Logger.getLogger(this.getClass()).info("Destroyed: " + dist.getName() + "," + dist.getId());

    }

    @Override
    public void onMessage(Message<Object> msg) {
        this.localQueue.add((TinyCrawlable) msg.getMessageObject());
    }   
}
