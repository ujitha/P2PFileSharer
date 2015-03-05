package view;

/**
 * Created by udith on 3/5/15.
 */
public class DummyResponse {

    FSViewController viewController;

    public DummyResponse(FSViewController fsview) {
        viewController = fsview;
    }

    public String connect(String serverIP, int serverPort, String nodeIP, int nodePort){
//        return "fwefergier";

        String[] myFiles = new String[20];
        for(int i=0;i<myFiles.length;i++){
            myFiles[i] = "FileName "+i+".txt";
        }
        viewController.populateFileList(myFiles);

        return "OK";
    }

    public String disconnect(){
//        return "wefmpeowfme";
        return "OK";
    }

    public void search(String fileName){

    }
}
