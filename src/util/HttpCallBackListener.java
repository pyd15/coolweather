package util;

public interface HttpCallBackListener {
	void onFinish(String respone);
	void onError(Exception e);
}
