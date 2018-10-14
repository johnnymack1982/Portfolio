//
//  EventPhotoViewController.swift
//  Keep Calm and Calendar
//
//  Created by Johnny Mack on 10/6/18.
//  Copyright © 2018 John Mack. All rights reserved.
//

import UIKit

class EventPhotoViewController: UIViewController, UINavigationControllerDelegate, UIImagePickerControllerDelegate {
    
    
    
    // MARK: - UI Outlets
    @IBOutlet weak var eventNameLabel: UILabel!
    @IBOutlet weak var eventTimeLabel: UILabel!
    @IBOutlet weak var eventPhotoView: UIImageView!
    @IBOutlet weak var deleteEventButton: UIButton!
    @IBOutlet weak var createEventButton: UIButton!
    
    
    
    // MARK: - Class Properties
    var eventName: String?
    var dateTime: Date?
    var requiresCompletion = false
    var oneTimeEvent = false
    var recurrenceFrequency: Int?
    var eventPhoto: UIImage?
    var newEvent: Event?
    var selectedEvent: Event?
    var parentMode = false
    var deleteEvent = false
    
    
    
    // MARK: - System Generated Functions
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Populate header with new event name
        eventNameLabel.text = eventName
        
        // Populate header with new event time
        let calendar = Calendar.current
        let hour = calendar.component(.hour, from: dateTime!)
        let minute = calendar.component(.minute, from: dateTime!)
        
        if hour > 12 {
            eventTimeLabel.text = "\(hour - 12):\(String(format: "%02d", minute)) PM"
        }
            
        else {
            eventTimeLabel.text = "\(hour):\(String(format: "%02d", minute)) AM"
        }
        
        if parentMode == true {
            createEventButton.setTitle("Save Changes", for: .normal)
        }
        
        deleteEventButton.isHidden = true
        
        if parentMode == true {
            deleteEventButton.isHidden = false
            
            if selectedEvent?.image != #imageLiteral(resourceName: "Logo") {
                eventPhotoView.image = selectedEvent?.image
            }
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func shouldPerformSegue(withIdentifier identifier: String, sender: Any?) -> Bool {
        
        // If user supplied a photo, attach it to the event
        if let image = eventPhoto {
            newEvent = Event(name: eventName!, date: dateTime!, image: image, completion: requiresCompletion, recurrenceFrequency: recurrenceFrequency!, originalIndex: 0)
        }
        
        // If user did not supply a photo, attach default image
        else {
            newEvent = Event(name: eventName!, date: dateTime!, image: #imageLiteral(resourceName: "Logo"), completion: requiresCompletion, recurrenceFrequency: recurrenceFrequency!, originalIndex: 0)
        }
        
        return true
    }
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : Any]) {
        picker.dismiss(animated: true, completion: nil)
        
        guard let capturedImage = info[UIImagePickerControllerEditedImage] as? UIImage
        
            else {
                print("Invalid Image")
                return
        }
        
        eventPhoto = capturedImage
        eventPhotoView.image = eventPhoto
        
        if parentMode == true {
            selectedEvent?.image = eventPhoto!
        }
    }
    
    
    
    // MARK: - Action Functions
    @IBAction func takePhotoTapped(_ sender: UIButton) {
        let camera = UIImagePickerController()
        camera.sourceType = .camera
        camera.allowsEditing = true
        camera.delegate = self
        
        present(camera, animated: true)
        
        /*let alert = UIAlertController(title: "Coming Soon", message: "Pardon our dust. This feature isn't ready just yet!", preferredStyle: .alert)
        let okButton = UIAlertAction(title: "OK", style: .default, handler: nil)
        alert.addAction(okButton)
        self.present(alert, animated: true)*/
    }
    
    @IBAction func deleteButtonTapped(_ sender: UIButton) {
        let alert = UIAlertController(title: "Delete Event?", message: "Are you sure you want to permanently remove this event? All events in the series will also be removed.", preferredStyle: .alert)
        
        let cancelButton = UIAlertAction(title: "Cancel", style: .cancel, handler: nil)
        
        let deleteButton = UIAlertAction(title: "Delete", style: UIAlertActionStyle.destructive) { (deleteSelectedEvent) in
            self.deleteEvent = true
            self.performSegue(withIdentifier: "DeleteFromEventPhoto", sender: nil)
        }
        
        alert.addAction(cancelButton)
        alert.addAction(deleteButton)
        
        self.present(alert, animated: true, completion: nil)
    }
}
