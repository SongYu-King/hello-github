package com.busap.wowan.redis.exception;


public class ClusterNotServerException extends RuntimeException {

	private static final long serialVersionUID = -4361215623762651942L;
	
	public ClusterNotServerException(String msg) {
		super(msg);
	}

}
