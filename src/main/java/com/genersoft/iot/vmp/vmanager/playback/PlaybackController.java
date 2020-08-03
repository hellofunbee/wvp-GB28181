package com.genersoft.iot.vmp.vmanager.playback;

import com.alibaba.fastjson.JSONObject;
import com.genersoft.iot.vmp.gb28181.bean.Device;
import com.genersoft.iot.vmp.gb28181.transmit.cmd.impl.SIPCommander;
import com.genersoft.iot.vmp.storager.IVideoManagerStorager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class PlaybackController {

    private final static Logger logger = LoggerFactory.getLogger(PlaybackController.class);

    @Autowired
    private SIPCommander cmder;

    @Autowired
    private IVideoManagerStorager storager;

    @GetMapping("/playback/{deviceId}/{channelId}")
    public ResponseEntity<String> play(@PathVariable String deviceId, @PathVariable String channelId, String startTime, String endTime) {

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("设备回放 API调用，deviceId：%s ，channelId：%s", deviceId, channelId));
        }

        if (StringUtils.isEmpty(deviceId) || StringUtils.isEmpty(channelId)) {
            String log = String.format("设备回放 API调用失败，deviceId：%s ，channelId：%s", deviceId, channelId);
            logger.warn(log);
            return new ResponseEntity<String>(log, HttpStatus.BAD_REQUEST);
        }

        Device device = storager.queryVideoDevice(deviceId);
        String ssrc = cmder.playbackStreamCmd(device, channelId, startTime, endTime);

        if (logger.isDebugEnabled()) {
            logger.debug("设备回放 API调用，ssrc：" + ssrc + ",ZLMedia streamId:" + Integer.toHexString(Integer.parseInt(ssrc)));
        }

        if (ssrc != null) {
            JSONObject json = new JSONObject();
            json.put("ssrc", ssrc);
            return new ResponseEntity<String>(json.toString(), HttpStatus.OK);
        } else {
            logger.warn("设备回放API调用失败！");
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/playback/{ssrc}/stop")
    public ResponseEntity<String> playStop(@PathVariable String ssrc) {

        cmder.streamByeCmd(ssrc);

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("设备录像回放停止 API调用，ssrc：%s", ssrc));
        }

        if (ssrc != null) {
            JSONObject json = new JSONObject();
            json.put("ssrc", ssrc);
            return new ResponseEntity<String>(json.toString(), HttpStatus.OK);
        } else {
            logger.warn("设备录像回放停止API调用失败！");
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


	/**
	 * 控制回放视频流播放速度 TODO 校验 参数
	 *
	 * @param ssrc  媒体流的ssrc
	 * @param scale  倍速 0.25、0.5、1、2、4
	 */
    @PostMapping("/playback/{ssrc}/{scale}/speed")
    public ResponseEntity<String> playSpeed(@PathVariable String ssrc, @PathVariable String scale) {
        cmder.speedBackStreamCmd(ssrc, scale);
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("设备录像回放控制速度 API调用，ssrc：%s", ssrc));
        }
        if (ssrc != null) {
            JSONObject json = new JSONObject();
            json.put("ssrc", ssrc);
            return new ResponseEntity<String>(json.toString(), HttpStatus.OK);
        } else {
            logger.warn("设备录像回放速度API调用失败！");
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	/**
	 * 暂停播放回放命令 TODO 校验 参数
	 *
	 * @param ssrc  媒体流的ssrc
	 * @param isPause  1：暂停 2：播放
	 */
	@PostMapping("/playback/{ssrc}/{isPause}/pause")
	public ResponseEntity<String> playPause(@PathVariable String ssrc, @PathVariable String isPause) {
		cmder.pauseBackStreamCmd(ssrc, isPause);
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("设备录像回放暂停/播放 API调用，ssrc：%s", ssrc));
		}
		if (ssrc != null) {
			JSONObject json = new JSONObject();
			json.put("ssrc", ssrc);
			return new ResponseEntity<String>(json.toString(), HttpStatus.OK);
		} else {
			logger.warn("设备录像回放暂停/播放API调用失败！");
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


}
