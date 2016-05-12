package ch.templer.utils.logging;

import android.util.Log;

/**
 * Created by Templer on 4/27/2016.
 */
public class Logger {

    public enum LOGGER_DEPTH{
        ACTUAL_METHOD(4),
        LOGGER_METHOD(3),
        STACK_TRACE_METHOD(1),
        JVM_METHOD(0);

        private final int value;
        private LOGGER_DEPTH(final int newValue){
            value = newValue;
        }
        public int getValue(){
            return value;
        }
    }

    private static final String personalTAG = "Logger";

    private StringBuilder sb;

    private Logger(){
        if(LoggerLoader.instance != null){
            Log.e(personalTAG,"Error: Logger already instantiated");
            throw new IllegalStateException("Already Instantiated");
        }else{

            this.sb = new StringBuilder(255);
        }
    }

    public static Logger getLogger(){
        return LoggerLoader.instance;
    }

    private String getTag(LOGGER_DEPTH depth){
        try{
            String className = Thread.currentThread().getStackTrace()[depth.getValue()].getClassName();
            sb.append(className.substring(className.lastIndexOf(".")+1));
            sb.append("[");
            sb.append(Thread.currentThread().getStackTrace()[depth.getValue()].getMethodName());
            sb.append("] - ");
            sb.append(Thread.currentThread().getStackTrace()[depth.getValue()].getLineNumber());
            return sb.toString();
        }catch (Exception ex){
            ex.printStackTrace();
            Log.d(personalTAG, ex.getMessage());
        }finally{
            sb.setLength(0);
        }
        return null;
    }

    public void d(String msg){
        try {
            Log.d(getTag(LOGGER_DEPTH.ACTUAL_METHOD), msg);
        }catch (Exception exception){
            Log.e(getTag(LOGGER_DEPTH.ACTUAL_METHOD), "Logger failed, exception: "+exception.getMessage());
        }
    }

    public void d(String msg, LOGGER_DEPTH depth){
        try {
            Log.d(getTag(depth), msg);
        }catch (Exception exception){
            Log.e(getTag(LOGGER_DEPTH.ACTUAL_METHOD), "Logger failed, exception: "+exception.getMessage());
        }
    }

    public void d(String msg, Throwable t, LOGGER_DEPTH depth){
        try{
            Log.d(getTag(depth),msg,t);
        }catch (Exception exception){
            Log.e(getTag(LOGGER_DEPTH.ACTUAL_METHOD), "Logger failed, exception: "+exception.getMessage());
        }
    }

    public void e(String msg){
        try{
            Log.e(getTag(LOGGER_DEPTH.ACTUAL_METHOD),msg);

        }catch (Exception exception){
            Log.e(getTag(LOGGER_DEPTH.ACTUAL_METHOD), "Logger failed, exception: "+exception.getMessage());
        }
    }

    public void e(String msg, LOGGER_DEPTH depth){
        try{
            Log.e(getTag(depth),msg);

        }catch (Exception exception){
            Log.e(getTag(LOGGER_DEPTH.ACTUAL_METHOD), "Logger failed, exception: "+exception.getMessage());
        }
    }

    public void e(String msg, Throwable t, LOGGER_DEPTH depth){
        try{
            Log.e(getTag(depth),msg,t);

        }catch (Exception exception){
            Log.e(getTag(LOGGER_DEPTH.ACTUAL_METHOD), "Logger failed, exception: "+exception.getMessage());
        }
    }

    public void w(String msg){
        try {
            Log.w(getTag(LOGGER_DEPTH.ACTUAL_METHOD), msg);

        }catch (Exception exception){
            Log.e(getTag(LOGGER_DEPTH.ACTUAL_METHOD), "Logger failed, exception: "+exception.getMessage());
        }
    }

    public void w(String msg, LOGGER_DEPTH depth){
        try{
            Log.w(getTag(depth), msg);

        }catch (Exception exception){
            Log.e(getTag(LOGGER_DEPTH.ACTUAL_METHOD), "Logger failed, exception: "+exception.getMessage());
        }
    }

    public void w(String msg, Throwable t, LOGGER_DEPTH depth){
        try{
            Log.w(getTag(depth), msg, t);

        }catch (Exception exception){
            Log.e(getTag(LOGGER_DEPTH.ACTUAL_METHOD), "Logger failed, exception: "+exception.getMessage());
        }
    }

    public void v(String msg){
        try{
            Log.v(getTag(LOGGER_DEPTH.ACTUAL_METHOD), msg);

        }catch (Exception exception){
            Log.e(getTag(LOGGER_DEPTH.ACTUAL_METHOD), "Logger failed, exception: "+exception.getMessage());
        }
    }

    public void v(String msg, LOGGER_DEPTH depth){
        try{
            Log.v(getTag(depth), msg);

        }catch (Exception exception){
            Log.e(getTag(LOGGER_DEPTH.ACTUAL_METHOD), "Logger failed, exception: "+exception.getMessage());
        }
    }

    public void v(String msg, Throwable t, LOGGER_DEPTH depth){
        try{
            Log.v(getTag(depth), msg, t);

        }catch (Exception exception){
            Log.e(getTag(LOGGER_DEPTH.ACTUAL_METHOD), "Logger failed, exception: "+exception.getMessage());
        }
    }

    public void i(String msg){
        try{
            Log.i(getTag(LOGGER_DEPTH.ACTUAL_METHOD), msg);

        }catch (Exception exception){
            Log.e(getTag(LOGGER_DEPTH.ACTUAL_METHOD), "Logger failed, exception: "+exception.getMessage());
        }
    }

    public void i(String msg, LOGGER_DEPTH depth){
        try{
            Log.i(getTag(depth), msg);

        }catch (Exception exception){
            Log.e(getTag(LOGGER_DEPTH.ACTUAL_METHOD), "Logger failed, exception: "+exception.getMessage());
        }
    }

    public void i(String msg, Throwable t, LOGGER_DEPTH depth){
        try {
            Log.i(getTag(depth), msg, t);

        }catch (Exception exception){
            Log.e(getTag(LOGGER_DEPTH.ACTUAL_METHOD), "Logger failed, exception: "+exception.getMessage());
        }
    }

    public void wtf(String msg){
        try{
            Log.wtf(getTag(LOGGER_DEPTH.ACTUAL_METHOD), msg);

        }catch (Exception exception){
            Log.e(getTag(LOGGER_DEPTH.ACTUAL_METHOD), "Logger failed, exception: "+exception.getMessage());
        }
    }

    public void wtf(String msg, LOGGER_DEPTH depth){
        try{
            Log.wtf(getTag(depth), msg);

        }catch (Exception exception){
            Log.e(getTag(LOGGER_DEPTH.ACTUAL_METHOD), "Logger failed, exception: "+exception.getMessage());
        }
    }

    public void wtf(String msg, Throwable t, LOGGER_DEPTH depth){
        try{
            Log.wtf(getTag(depth), msg, t);

        }catch (Exception exception){
            Log.e(getTag(LOGGER_DEPTH.ACTUAL_METHOD), "Logger failed, exception: "+exception.getMessage());
        }
    }

    private static class LoggerLoader {
        private static final Logger instance = new Logger();
    }
}