package mixsomething.tang.di.handler;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import mixsomething.tang.di.http.TDdownLoad;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;


/**
 * 使用泛型实现Handler类，用于多个耗时任务进行，并与UI进行交互
 * @author tangdi
 *
 *TDHandler 能在主线程上完成任务的方式是，让主线程将其自身的Handler传递给HandlerThread
 *主线程是一个拥有handler和Loopr的消息循环，主线程上创建的Handler会自动与它的Looper相关联。我们可以将主线程
 *上创建的Handler传递给另一个线程。传递出去的Handler与创建它的线程Lopper始终保持联系，因此，任何已传出的Handler负责
 *处理的消息都将在主线程的消息队列中处理
 */
public class TDHandler<Token> extends HandlerThread{
	
	private static final String TAG = "===TDHandler<Token>====";

	private Handler tDHandler;
	
	/**
	 * 来自主线程的Handler
	 */
	private Handler mReqHandler;
	
	private Listener<Token> mListener;
	
	private static int MESSAGE_DOWNLOAD = 1;
	
	private Map<Token, String> requestMap = Collections.synchronizedMap(new HashMap<Token, String>());
	
	public TDHandler(Handler mReqHandler) {
		super(TAG);
		this.mReqHandler = mReqHandler;
	}
	/**
     * Call back method that can be explicitly overridden if needed to execute some
     * setup before Looper loops.
     * 
     * onLooperPerpared方法的调用是在Looper第一次检查消息队列之前
     * 所以该方法是完成我们自己的Handler实现最好的地方
     */
	@SuppressLint("HandlerLeak")
	@Override
	protected void onLooperPrepared() {
		
		tDHandler = new Handler(){
			@Override
			public void handleMessage(Message msg){
				
				if(msg.what == MESSAGE_DOWNLOAD){
					/**
					 * 处理下载任务
					 */
					@SuppressWarnings({ "unchecked" })
					Token token = (Token)msg.obj;
					tDhandlerRequest(token);
				}
			}
		};
    }
	
	private void tDhandlerRequest(final Token token){
		try{
			final String url = requestMap.get(token);
			if(url == null){
				return;
			}
			byte[] bitmapBytes = new TDdownLoad().getUrlBytes(url);
			final Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
			Log.d(TAG, "图片下载成功");
			
			/**
			 * Handler是绑定到创建它的主线程中的，Handler跟UI主线程是同一个线程。
			 * Handler的作用：主要是在其他后台线程中，通过handler这个媒介,
			 * 向UI主线程发送Runnable对象（即代码段）
			 * 其中传入的Runable并不是开启线程，而是向主线程发送了一个代码片段
			 */
			mReqHandler.post(new Runnable() {
				
				@Override
				public void run() {
					if(requestMap.get(token) != url){
						return;
					}
					requestMap.remove(token);
					/**
					 * 更改UI代码，必须在主线程中完成，所以必须通过mReqHandler完成代码的发送
					 */
					mListener.onThumbnailDownloaded(token, bitmap);
				}
			});
		}catch (IOException e){
			Log.e(TAG, "图片下载失败");
		}
	}
	
	
	/**
	 * 暴露的接口，用于获取需要下载内容的详情
	 * 
	 */
	public void queueThumbnail(Token token, String url){
		requestMap.put(token, url);
		
		/**
		 * 发送消息内容
		 */
		tDHandler.obtainMessage(MESSAGE_DOWNLOAD, token)
		.sendToTarget();
	}
	
	/**
	 * 暴露的接口，通过回调方法来实现主线程中的UI更新
	 * @param
	 */
	public interface Listener<Token>{
		void onThumbnailDownloaded(Token token, Bitmap bitmap);
	}
	
	/**
	 * 在用户需要的地方实现Listener接口，并且通过该方法将接口的实现传入该类中，从而实现回调
	 * @param listener
	 */
	public void setListener(Listener<Token> listener){
		mListener = listener;
	}
	
	
	/**
	 * 清楚队列外的所有请求，
	 * 在视图结束的时候调用该方法
	 * onDestroyView（）
	 */
	public void clearQueue(){
		tDHandler.removeMessages(MESSAGE_DOWNLOAD);
		requestMap.clear();
	}

}
