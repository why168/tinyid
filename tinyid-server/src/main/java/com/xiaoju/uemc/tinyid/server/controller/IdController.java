package com.xiaoju.uemc.tinyid.server.controller;

import com.xiaoju.uemc.tinyid.base.entity.SegmentId;
import com.xiaoju.uemc.tinyid.base.generator.IdGenerator;
import com.xiaoju.uemc.tinyid.base.service.SegmentIdService;
import com.xiaoju.uemc.tinyid.server.factory.impl.IdGeneratorFactoryServer;
import com.xiaoju.uemc.tinyid.server.service.TinyIdTokenService;
import com.xiaoju.uemc.tinyid.server.vo.ErrorCode;
import com.xiaoju.uemc.tinyid.server.vo.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author du_imba
 */
@RestController
@RequestMapping("/id")
public class IdController {

    private static final Logger logger = LoggerFactory.getLogger(IdController.class);

    @Autowired
    private IdGeneratorFactoryServer idGeneratorFactoryServer;
    @Autowired
    private SegmentIdService segmentIdService;
    @Autowired
    private TinyIdTokenService tinyIdTokenService;
    @Value("${batch.size.max}")
    private Integer batchSizeMax;

    @GetMapping("/nextId")
    public Response<List<Long>> nextId(@RequestParam String bizType,
                                        @RequestParam(required = false) Integer batchSize,
                                        @RequestParam String token) {
        Response<List<Long>> response = new Response<>();
        Integer newBatchSize = checkBatchSize(batchSize);
        if (!tinyIdTokenService.canVisit(bizType, token)) {
            response.setCode(ErrorCode.TOKEN_ERR.getCode());
            response.setMessage(ErrorCode.TOKEN_ERR.getMessage());
            return response;
        }
        try {
            IdGenerator idGenerator = idGeneratorFactoryServer.getIdGenerator(bizType);
            List<Long> ids = idGenerator.nextId(newBatchSize);
            response.setData(ids);
        } catch (Exception e) {
            response.setCode(ErrorCode.SYS_ERR.getCode());
            response.setMessage(e.getMessage());
            logger.error("nextId error", e);
        }
        return response;
    }

    private Integer checkBatchSize(Integer batchSize) {
        if (batchSize == null) {
            batchSize = 1;
        }
        if (batchSize > batchSizeMax) {
            batchSize = batchSizeMax;
        }
        return batchSize;
    }

    @GetMapping("/nextIdSimple")
    public String nextIdSimple(@RequestParam String bizType,
                               @RequestParam(required = false) Integer batchSize,
                               @RequestParam String token) {
        Integer newBatchSize = checkBatchSize(batchSize);
        if (!tinyIdTokenService.canVisit(bizType, token)) {
            return "";
        }
        try {
            IdGenerator idGenerator = idGeneratorFactoryServer.getIdGenerator(bizType);
            if (newBatchSize == 1) {
                return String.valueOf(idGenerator.nextId());
            } else {
                List<Long> idList = idGenerator.nextId(newBatchSize);
                return idList.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(","));
            }
        } catch (Exception e) {
            logger.error("nextIdSimple error", e);
        }
        return "";
    }

    @GetMapping("/nextSegmentId")
    public Response<SegmentId> nextSegmentId(@RequestParam String bizType,
                                              @RequestParam String token) {
        Response<SegmentId> response = new Response<>();
        if (!tinyIdTokenService.canVisit(bizType, token)) {
            response.setCode(ErrorCode.TOKEN_ERR.getCode());
            response.setMessage(ErrorCode.TOKEN_ERR.getMessage());
            return response;
        }
        try {
            SegmentId segmentId = segmentIdService.getNextSegmentId(bizType);
            response.setData(segmentId);
        } catch (Exception e) {
            response.setCode(ErrorCode.SYS_ERR.getCode());
            response.setMessage(e.getMessage());
            logger.error("nextSegmentId error", e);
        }
        return response;
    }

    @GetMapping("/nextSegmentIdSimple")
    public String nextSegmentIdSimple(@RequestParam String bizType,
                                      @RequestParam String token) {
        if (!tinyIdTokenService.canVisit(bizType, token)) {
            return "";
        }
        try {
            SegmentId segmentId = segmentIdService.getNextSegmentId(bizType);
            return segmentId.getCurrentId() + "," + segmentId.getLoadingId() + "," + segmentId.getMaxId()
                    + "," + segmentId.getDelta() + "," + segmentId.getRemainder();
        } catch (Exception e) {
            logger.error("nextSegmentIdSimple error", e);
        }
        return "";
    }
}
