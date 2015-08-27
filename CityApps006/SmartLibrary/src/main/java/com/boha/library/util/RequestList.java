package com.boha.library.util;

import com.boha.library.transfer.RequestDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aubreyM on 15/06/20.
 */
public class RequestList {
    private List<RequestDTO> requestList = new ArrayList<>();

    public List<RequestDTO> getRequestList() {
        return requestList;
    }

    public void setRequestList(List<RequestDTO> requestList) {
        this.requestList = requestList;
    }
}
