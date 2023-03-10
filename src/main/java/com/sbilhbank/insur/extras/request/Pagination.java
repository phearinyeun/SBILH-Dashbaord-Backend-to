package com.sbilhbank.insur.extras.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Pagination {
    @NotNull
    private int page;
    @NotNull
    private int size;
}