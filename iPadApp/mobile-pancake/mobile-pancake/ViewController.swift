//
//  ViewController.swift
//  mobile-pancake
//
//  Created by Alban Fayard on 27/10/15.
//  Copyright © 2015 Alban Fayard. All rights reserved.
//

import UIKit
import AVFoundation


class ViewController: UIViewController, AVCaptureMetadataOutputObjectsDelegate, UITextFieldDelegate {
    
    var captureSession:AVCaptureSession?
    var videoPreviewLayer:AVCaptureVideoPreviewLayer?
    var qrCodeFrameView:UIView?
    var selectedIngredient = ""
    var selectedInstrument = ""
    var testingField: UITextField?
    
    @IBOutlet weak var messageLabel: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        // Get an instance of the AVCaptureDevice class to initialize a device object and provide the video
        // as the media type parameter.
        let captureDevice = AVCaptureDevice.defaultDeviceWithMediaType(AVMediaTypeVideo)
        
        var input:AVCaptureDeviceInput?
        do {
            input = try AVCaptureDeviceInput(device: captureDevice)
            // Do the rest of your work...
        } catch let error as NSError {
            // Handle any errors
            print(error)
            return
        }
        
        // Initialize the captureSession object.
        captureSession = AVCaptureSession()
        // Set the input device on the capture session.
        captureSession?.addInput(input)
        
        // Initialize a AVCaptureMetadataOutput object and set it as the output device to the capture session.
        let captureMetadataOutput = AVCaptureMetadataOutput()
        captureSession?.addOutput(captureMetadataOutput)
        
        // Set delegate and use the default dispatch queue to execute the call back
        captureMetadataOutput.setMetadataObjectsDelegate(self, queue: dispatch_get_main_queue())
        captureMetadataOutput.metadataObjectTypes = [AVMetadataObjectTypeQRCode]
        
        // Initialize the video preview layer and add it as a sublayer to the viewPreview view's layer.
        addVideoPreviewView()
        
        // Start video capture.
        captureSession?.startRunning()
        
        // Move the message label to the top view
        view.bringSubviewToFront(messageLabel)
        
        addQRCodeView()
        

    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func addVideoPreviewView() {
        videoPreviewLayer = AVCaptureVideoPreviewLayer(session: captureSession)
        videoPreviewLayer?.videoGravity = AVLayerVideoGravityResizeAspectFill
        videoPreviewLayer?.frame = view.layer.bounds
        view.layer.addSublayer(videoPreviewLayer!)
    }
    
    func addQRCodeView() {
        // Initialize QR Code Frame to highlight the QR code
        qrCodeFrameView = UIView()
        qrCodeFrameView?.layer.borderColor = UIColor.greenColor().CGColor
        qrCodeFrameView?.layer.borderWidth = 2
        view.addSubview(qrCodeFrameView!)
        view.bringSubviewToFront(qrCodeFrameView!)
    }
    
    func captureOutput(captureOutput: AVCaptureOutput!, didOutputMetadataObjects metadataObjects: [AnyObject]!, fromConnection connection: AVCaptureConnection!) {
        
        // Check if the metadataObjects array is not nil and it contains at least one object.
        if metadataObjects == nil || metadataObjects.count == 0 {
            qrCodeFrameView?.frame = CGRectZero
            return
        }
        
        // Get the metadata object.
        let metadataObj = metadataObjects[0] as! AVMetadataMachineReadableCodeObject
        
        if metadataObj.type == AVMetadataObjectTypeQRCode {
            // If the found metadata is equal to the QR code metadata then update the status label's text and set the bounds
            let barCodeObject = videoPreviewLayer?.transformedMetadataObjectForMetadataObject(metadataObj as AVMetadataMachineReadableCodeObject) as! AVMetadataMachineReadableCodeObject
            qrCodeFrameView?.frame = barCodeObject.bounds;
            
            if metadataObj.stringValue != nil  {
                if (selectedInstrument.isEmpty) {
                    let alert = UIAlertController(title: "Instrument selection", message: "Selected instrument: " + metadataObj.stringValue, preferredStyle: UIAlertControllerStyle.Alert)
                    alert.addAction(UIAlertAction(title: "OK", style: UIAlertActionStyle.Default, handler: nil))
                    selectedInstrument = metadataObj.stringValue
                    messageLabel.text = "Selected instrument: " + selectedInstrument
                    self.presentViewController(alert, animated: true, completion: nil)
                } else if (selectedIngredient.isEmpty) {
                    
                    if (selectedInstrument == metadataObj.stringValue) {
                        selectedIngredient = "";
                    } else {
                        let alert = UIAlertController(title: "Ingredient selection", message: "Selected ingredient: " + metadataObj.stringValue, preferredStyle: UIAlertControllerStyle.Alert)
                        alert.addAction(UIAlertAction(title: "OK", style: UIAlertActionStyle.Default, handler: nil))
                        selectedIngredient = metadataObj.stringValue
                        messageLabel.text = "Selected ingredient: " + selectedIngredient
                        self.presentViewController(alert, animated: true, completion: nil)
                    }
                }
            }
        }
    }

    func addTextField(textField: UITextField!){
        // add the text field and make the result global
        textField.placeholder = "Definition"
        textField.delegate = self //REQUIRED
        textField.placeholder = "Enter your value"
        self.testingField = textField
    }
    
    @IBAction func sendDataToServer() {
        let alert = UIAlertController(title: "Enter a value", message: "Measurement: ", preferredStyle: UIAlertControllerStyle.Alert)
        
        alert.addTextFieldWithConfigurationHandler(addTextField)
        alert.addAction(UIAlertAction(title: "Submit", style: UIAlertActionStyle.Default, handler: postValue))
        self.presentViewController(alert, animated: true, completion: nil)

    }
    
    lazy var config: NSURLSessionConfiguration = NSURLSessionConfiguration.defaultSessionConfiguration()
    lazy var session: NSURLSession = NSURLSession(configuration: self.config)
    let queryURL = NSURL(string: "http://10.52.4.100:8080/instrument/writeMeasure")
    
    func postValue(action: UIAlertAction) {
        let request:NSMutableURLRequest = NSMutableURLRequest(URL:queryURL!)
        let text = testingField?.text;
        let bodyData = "{instrumentName:'" + selectedInstrument + "',value:'" + text! + "', ingredientName:'" + selectedIngredient + "'}"

        request.HTTPMethod = "POST"
        request.HTTPBody = bodyData.dataUsingEncoding(NSUTF8StringEncoding, allowLossyConversion: true);
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        
        let dataTask = session.dataTaskWithRequest(request) {
            (let data: NSData?, let response: NSURLResponse?, let error: NSError?) -> Void in
            
            // 1: Check HTTP Response for successful GET request
            guard let _ = response as? NSHTTPURLResponse, _ = data
                else {
                    print("error: not a valid http response")
                    return
            }
            
        }
        dataTask.resume()

    }
    
    @IBAction func rest() {
        
        let request = NSURLRequest(URL: queryURL!)
        
        let dataTask = session.dataTaskWithRequest(request) {
            (let data: NSData?, let response: NSURLResponse?, let error: NSError?) -> Void in
            
            // 1: Check HTTP Response for successful GET request
            guard let httpResponse = response as? NSHTTPURLResponse, receivedData = data
                else {
                    print("error: not a valid http response")
                    return
            }
            
            switch (httpResponse.statusCode) {
            case 200:
                // 2: Create JSON object with data
                do {
                    
                    let jsonDictionary = try NSJSONSerialization.JSONObjectWithData(receivedData, options: NSJSONReadingOptions.MutableLeaves) as! NSArray

                    self.completion(jsonDictionary)
                } catch {
                    print("error parsing json data")
                }
            default:
                print("GET request got response \(httpResponse.statusCode)")
            }
        }
        dataTask.resume()
    }
   
    func completion(jsonDictionary:NSArray?) {
        
        for (feed) in jsonDictionary! {
            if ((feed as? NSDictionary) != nil) {
                if let name = feed["name"] as? NSString {
                    print(name)
                }
                if let qrcode = feed["qrCode"] as? NSString {
                    print(qrcode)
                }
                
            }
        }
        

    }
    
}
