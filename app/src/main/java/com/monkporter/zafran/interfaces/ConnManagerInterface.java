package com.monkporter.zafran.interfaces;

public interface ConnManagerInterface {
	public void successResponseProcess(final String response);
	public void failedResponseProcess(final String response);
	public void executeError(final String error);


}
  