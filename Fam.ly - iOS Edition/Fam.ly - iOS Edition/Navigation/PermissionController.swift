//
//  PermissionController.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 5/22/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import UIKit

public class PermissionController: UIViewController, UITextFieldDelegate, UITableViewDataSource, UITableViewDelegate {
    
    
    
    // UI Outlets
    @IBOutlet weak var inputHeight: NSLayoutConstraint!
    @IBOutlet weak var requestMessageInput: UITextField!
    
    @IBOutlet weak var postButtonsHeight: NSLayoutConstraint!
    @IBOutlet weak var cancelButton: UIButton!
    @IBOutlet weak var sendButton: UIButton!
    
    @IBOutlet weak var requestList: UITableView!
    
    
    
    // Class properties
    var mChild: Child?
    var mParent: Parent?
    
    let refreshControl = UIRefreshControl()
    
    
    
    
    // System generated methods
    override public func viewDidLoad() {
        super.viewDidLoad()
        
        // Load profile
        mChild = AccountUtils.loadChild()
        mParent = AccountUtils.loadParent()
        
        // If profile is a parent, hide input field
        if mParent != nil {
            inputHeight.constant = 0
            requestMessageInput.isHidden = true
        }
        
        // If profile is a child, show input field
        else if mChild != nil {
            inputHeight.constant = 50
            requestMessageInput.isHidden = false
        }
        
        // Set default UI states
        postButtonsHeight.constant = 0
        
        cancelButton.isHidden = true
        sendButton.isHidden = true
        
        requestMessageInput.delegate = self
        requestList.delegate = self
        requestList.dataSource = self
    }
    
    public override func viewWillAppear(_ animated: Bool) {
        
        // Reference pull-down refressher
        if #available(iOS 10.0, *) {
            requestList.refreshControl = refreshControl
        } else {
            requestList.addSubview(refreshControl)
        }
        refreshControl.tintColor = UIColor.orange
        refreshControl.addTarget(self, action: #selector(refreshRequestList(_:)), for: .valueChanged)
        
        // Refresh request list display
        requestList.reloadData()
    }
    
    public func textFieldDidBeginEditing(_ textField: UITextField) {
        if textField.tag == 0 {
            
            // While input field is active, show send and cancel buttons
            postButtonsHeight.constant = 50
            
            cancelButton.isHidden = false
            sendButton.isHidden = false
        }
    }
    
    public func textFieldDidEndEditing(_ textField: UITextField) {
        if textField.tag == 0 {
            
            // While text field is inactive, hide send and cancel buttons
            postButtonsHeight.constant = 0
            
            cancelButton.isHidden = true
            sendButton.isHidden = true
        }
    }
    
    public func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        let nextTag = textField.tag + 1
        
        // On 'enter,' move to next input field. If last field, dismiss keyboard
        if let nextResponder = textField.superview?.viewWithTag(nextTag) {
            nextResponder.becomeFirstResponder()
        } else {
            textField.resignFirstResponder()
        }
        
        return true
    }
    
    
    
    // Tableview methods
    public func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    public func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        // Return number of requests in list
        return PermissionRequestUtils.loadRequests().count
    }
    
    public func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        var cell: PermissionCell?
        
        // If profile is a parent...
        if mParent != nil {
            
            // Reference parent cell
            cell = requestList.dequeueReusableCell(withIdentifier: "parentRequestCell") as? PermissionCell
            
            // Load profile photo for requesting profile
            AccountUtils.loadProfilePhoto(ProfileId: (mParent?.getProfileId())!, ProfilePhoto: cell!.profilePhoto)
            
            // Reference current request
            let request = PermissionRequestUtils.loadRequests()[indexPath.row]
            
            // Display name of requester
            cell!.profileNameDisplay.text = request.getRequesterName() + " " + (AccountUtils.loadAccount()?.getFamilyName())!
            
            // Display timestamp
            let dateFormatter = DateFormatter()
            dateFormatter.dateFormat = "MMMM dd, yyyy @ hh:mm a"
            let dateString = dateFormatter.string(from: request.getTimeStamp())
            cell?.timeStampDisplay.text = dateString
            
            // Display request message
            cell?.requestMessageDisplay.text = request.getRequestMessage()
            
            // If request is denied, hide approve button
            if request.getRequestStatus() == -1 {
                cell?.grantButton.isHidden = true
                cell?.grantButton.isEnabled = false
                
                cell?.denybutton.alpha = 1
                cell?.denybutton.isEnabled = false
            }
            
            // If request is pending, show and enable both buttons
            else if request.getRequestStatus() == 0 {
                cell?.grantButton.isHidden = false
                cell?.grantButton.isEnabled = true
                cell?.grantButton.alpha = 0.25
                
                cell?.denybutton.isHidden = false
                cell?.denybutton.isEnabled = true
                cell?.denybutton.alpha = 0.25
            }
                
            // If current parent has approved request, hide deny button
            else if request.getFirstApprover() != nil && request.getFirstApprover() == mParent?.getProfileId() {
                cell?.grantButton.isHidden = false
                cell?.grantButton.isEnabled = false
                cell?.grantButton.alpha = 1
                
                cell?.denybutton.isHidden = true
                cell?.denybutton.isEnabled = false
            }
                
            // Show and enable both buttons by default
            else {
                cell?.grantButton.isHidden = false
                cell?.grantButton.isEnabled = true
                cell?.grantButton.alpha = 0.25
                
                cell?.denybutton.isHidden = false
                cell?.denybutton.isEnabled = true
                cell?.denybutton.alpha = 0.25
            }
        }
        
        // If profile is a child...
        else if mChild != nil {
            
            // Reference child cell
            cell = requestList.dequeueReusableCell(withIdentifier: "childRequestCell") as? PermissionCell
            
            // Load profile photo
            AccountUtils.loadProfilePhoto(ProfileId: (mChild?.getProfileId())!, ProfilePhoto: cell!.profilePhoto)
            
            // Reference current request
            let request = PermissionRequestUtils.loadRequests()[indexPath.row]
            
            // Display requester name
            cell!.profileNameDisplay.text = request.getRequesterName() + " " + (AccountUtils.loadAccount()?.getFamilyName())!
            
            // Display timestamp
            let dateFormatter = DateFormatter()
            dateFormatter.dateFormat = "MMMM dd, yyyy @ hh:mm a"
            let dateString = dateFormatter.string(from: request.getTimeStamp())
            cell?.timeStampDisplay.text = dateString
            
            // Display request message
            cell?.requestMessageDisplay.text = request.getRequestMessage()
            
            // If permission is pending, display pending icon
            if request.getRequestStatus() == 0 {
                cell?.requestStatusDisplay.image = UIImage(named: "PermissionPendingIcon")
            }
            
            // If permission is denied, display denied icon
            else if request.getRequestStatus() == -1 {
                cell?.requestStatusDisplay.image = UIImage(named: "PermissionDeniedIcon")
            }
            
            // If permission is approved, display approved icon
            else if request.getRequestStatus() == AccountUtils.loadAccount()?.getParents().count {
                cell?.requestStatusDisplay.image = UIImage(named: "PermissionGrantedIcon")
            }
        }
        
        // Round profile image view
        cell!.profilePhoto.layer.borderWidth = 1.0
        cell!.profilePhoto.layer.masksToBounds = false
        cell!.profilePhoto.layer.borderColor = UIColor.white.cgColor
        cell!.profilePhoto.layer.cornerRadius = cell!.profilePhoto.frame.size.width / 2
        cell!.profilePhoto.clipsToBounds = true
        
        return cell!
    }
    
    
    
    // Action methods
    @IBAction func buttonClicked(_ sender: UIButton) {
        switch sender.tag {
            
        // If cancel button is clicked, reset UI
        case 0:
            postButtonsHeight.constant = 0
            
            cancelButton.isHidden = true
            sendButton.isHidden = true
            
            requestMessageInput.resignFirstResponder()
            
        // If send button is clicked...
        case 1:
            if requestMessageInput.text != nil && requestMessageInput.text != "" {
                
                // Send request
                PermissionRequestUtils.sendRequest(Controller: self, RequestMessage: requestMessageInput.text!, Requester: mChild!)
                
                // Reset UI
                postButtonsHeight.constant = 0
                
                cancelButton.isHidden = true
                sendButton.isHidden = true
                
                requestMessageInput.resignFirstResponder()
                requestMessageInput.text = ""
            }
            
        default:
            print("Invalid button")
        }
    }
    
    @IBAction func responseButtonClicked(_ sender: UIButton) {
        
        // Reference cell that button was clicked in and associated request
        let cell = sender.superview?.superview as? PermissionCell
        let indexPath = requestList.indexPath(for: cell!)
        let request = PermissionRequestUtils.loadRequests()[(indexPath?.row)!]
        
        switch sender.tag {
            
        // If request is approved send response and visually indicate approval
        case 0:
            PermissionRequestUtils.approveRequest(Controller: self, Request: request)
            
            cell?.grantButton.alpha = 100
            cell?.grantButton.isEnabled = false
            
            cell?.denybutton.isHidden = true
            cell?.denybutton.isEnabled = false
            
        // If request is denied, send response and visually indicate denial
        case 1:
            PermissionRequestUtils.denyRequest(Controller: self, Request: request)
            
            cell?.denybutton.alpha = 100
            cell?.denybutton.isEnabled = false
            
            cell?.grantButton.isHidden = true
            cell?.grantButton.isEnabled = false
            
        default:
            print("Invalid button")
        }
    }
    
    @objc private func refreshRequestList(_ sender: Any) {
        
        // Refresh list
        requestList.reloadData()
        refreshControl.endRefreshing()
    }
}
