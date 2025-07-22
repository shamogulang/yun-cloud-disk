package cn.yun.oddworld.dto;

import lombok.Data;

import java.util.List;
@Data
public class ListByIdsReq {

    private List<Long> fileIds;
    private Long userId;
}
