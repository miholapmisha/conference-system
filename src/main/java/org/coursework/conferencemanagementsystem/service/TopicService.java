package org.coursework.conferencemanagementsystem.service;

import org.coursework.conferencemanagementsystem.entity.Topic;
import org.coursework.conferencemanagementsystem.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicService {

    private TopicRepository topicRepository;

    @Autowired
    public void setTopicRepository(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public List<Topic> getAllTopics() {
        return topicRepository.getAllTopics();
    }

    public List<Topic> getTopicsBySubmissionId(Long submissionId) {
        return topicRepository.getTopicsBySubmissionId(submissionId);
    }
}
