/*
 * The MIT License
 *
 * Copyright 2022 Robin.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.tendiesinc.robinstoolbox;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Robin Saunders
 *     
    This class is just a simple log output to shorthand a bunch of log/write operations. Saves me writing System.out.println(); with no timestamp, and instead I can just do p.log();
    
    Implementation Example (at start of class file):
    private Print p = Print.getInstance(); 
    
    Usage example:
    p.log("What's up party people!");
    
    Buffer usage example:
    p.startBuffer();
    while(condition){
        p.log("A quick example");
        p.toWrite("This is only is written to log file");
    }
    p.stopBuffer();
 * 
 * 
 */

public class Print {
    private static Print single_instance = null;
    private boolean isDebugging = false;
    private boolean buffering = false;
    private String buffer = "~~~~~~ Start of new Print() instance ~~~~~~";
    
    // Useful to keep instance count low. Typically don't need a new instance every time we're writing / outputting data.
    public static Print getInstance(){
        if (single_instance == null){single_instance = new Print();}
        
        return single_instance;
    }
    
    // Main function used, supposed to translate any general object input into it's string output. 
    public void log (Object entry){
        systemOutTimeStamped(entry.toString(), true);
    }
    
    // For outputting text to a UI interface. Requires some customization
    public void toGui(Object entry){
        // (Some sort of parent handler here).jLogTextArea.append("\n"+entry.toString());
        log("Not implemented for this project yet");
    }

    public void toBoth(Object entry){
        // (Some sort of parent handler here).jLogTextArea.append("\n"+entry.toString());
        systemOutTimeStamped(entry.toString(), true);
    }
    
    // For only writing to file and not terminal output. Use with buffer start/stop. Otherwise call writeToFile directly
    public void toWrite(Object entry){
        systemOutTimeStamped(entry.toString(), false);
    }
    
    public void logNoTimeStamp (Object entry){
        System.out.println(entry);
        if (buffering){
            bufferStorage(entry.toString());
        }
    }
    
    // For use with overarching debug switches
    public void debug (Object entry){
        if (isDebugging){
            systemOutTimeStamped(entry.toString(), true);
        }
    }
    
    public boolean getDebug() {
        return isDebugging;
    }

    public void setDebug(boolean debugging) {
        this.isDebugging = debugging;
    }
    
    // For getting the normal log output back instead of putting into terminal/log/logfile
    public String logCallBack (Object entry){
        return "[" + new Timestamp(System.currentTimeMillis()).toString().split(" ")[1] + "]" + entry.toString();
    }
    
    // For blocks of text you want to save to file, useful for looped methods that you want to write to a file afterwards. Saves doing lots of small writes.
    public void startBuffer(){
        buffering=true;
    }
    
    // Stop buffer will then write everything to file.
    public void stopBuffer(){
        buffering=false;
        writeToFile(buffer);
    }
    
    private void bufferStorage(String data){
        buffer = buffer+data+"\n";
    }
    
    public void writeToFile(String data){
        // Inline date formatter + getter
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");Date date = new Date();String today = dateFormat.format(date);
        try {
            // Get current working directory and create a folder for current day            
            File file =new File(System.getProperty("user. dir")+today+"\\printOutput.txt");
            if (!file.exists()){
                file.mkdir();
            }
            FileWriter fw;
            fw = new FileWriter(file,true); //Here true is to append the content to file
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(data+"\n");
            bw.close();
            buffer="";
        } catch (IOException ex) {
            Logger.getLogger(Print.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void systemOutTimeStamped(Object message, boolean printOut){
        try{
            String strMessage = "[" + new Timestamp(System.currentTimeMillis()).toString().split(" ")[1] + "]" + message.toString();
            if (buffering)bufferStorage(strMessage.toString());
            if (printOut)System.out.println(strMessage.toString());
        }catch(Exception e){
            System.out.println(message.toString());
            e.printStackTrace();
        }
    }
}
