package com.apachecon.memories.service;

public interface ApprovalService {

    boolean isAproved(String fileName);

    void aprove(String fileName);

    void decline(String fileName);

}
