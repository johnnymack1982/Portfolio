//
//  PermissionRequestUtils.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 6/12/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import Foundation
import FirebaseAuth
import FirebaseFirestore

public class PermissionRequestUtils {
    
    
    
    // Class properties
    
    
    
    // Custom methods
    // Custom method to send permittion request
    public static func sendRequest(Controller controller: PermissionController, RequestMessage requestMessage: String, Requester requester: Child) {
        
        // Reference database
        let database = Firestore.firestore()
        
        // Build request from user input and map for upload
        let request = Request(RequesterName: requester.getFirstName(), RequesterId: requester.getProfileId(), RequestMessage: requestMessage, TimeStamp: Date())
        let requestData: [String: Any] = [
            "requesterName": request.getRequesterName(),
            "requesterId": request.getRequesterId(),
            "requestMessage": request.getRequestMessage(),
            "requestStatus": request.getRequestStatus(),
            "timeStamp": request.getTimeStamp(),
            "firstApprover": request.getFirstApprover() as Any
        ]
        
        // Reference database location for child profile and attempt to upload
        database.collection("accounts").document(Auth.auth().currentUser!.uid).collection("profiles").document(Auth.auth().currentUser!.uid + requester.getProfileId()).collection("requests").document(request.getRequestId()).setData(requestData) { err in
            if let err = err {
                print("Error writing document: \(err)")
            }
            
            // If successful...
            else {
                
                // Save request to file
                var requests = loadRequests()
                requests.append(request)
                saveRequests(Requests: requests)
                
                // Load account
                let account = AccountUtils.loadAccount()
                
                // Let user know request was sent successfully
                let alert = UIAlertController(title: "Success", message: "Request sent", preferredStyle: .alert)
                alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
                controller.present(alert, animated: true)
                
                // Upload request to each parent profile
                for parent in (account?.getParents())! {
                    database.collection("accounts").document(Auth.auth().currentUser!.uid).collection("profiles").document(Auth.auth().currentUser!.uid + parent.getProfileId()).collection("incomingRequests").document(request.getRequestId()).setData(requestData) { err in
                        if let err = err {
                            print("Error writing document: \(err)")
                        }
                        
                        else {
                            print("Document successfully written!")
                        }
                    }
                }
            }
        }
    }
    
    // Custom method to receive incoming requests
    public static func receiveRequests(Parent parent: Parent) {
        
        // Reference database location and listen for updates
        let database = Firestore.firestore()
        database.collection("accounts").document(Auth.auth().currentUser!.uid).collection("profiles").document(Auth.auth().currentUser!.uid + (parent.getProfileId())).collection("incomingRequests").addSnapshotListener { querySnapshot, error in
            guard let documents = querySnapshot?.documents
                else {
                print("Error fetching documents: \(error!)")
                return
            }
            
            var requests = [Request]()
            
            // If successful, loop through requests and save to file
            for document in documents {
                let firstApprover = document.get("firstApprover") as? String?
                let requestMessage = document.get("requestMessage") as? String
                let requestStatus = document.get("requestStatus") as? Int
                let requesterId = document.get("requesterId") as? String
                let requesterName = document.get("requesterName") as? String
                let timeStamp = document.get("timeStamp") as? Date
                
                let request = Request(RequesterName: requesterName!, RequesterId: requesterId!, RequestMessage: requestMessage!, TimeStamp: timeStamp!)
                request.setRequestStatus(RequestStatus: requestStatus!)
                
                if firstApprover != nil && firstApprover != "" {
                    request.setFirstApprover(FirstApprover: firstApprover!)
                }
                
                requests.append(request)
            }
            
            saveRequests(Requests: requests)
        }
    }
    
    // Custom method to receive request responses
    public static func receiveResponses(Child child: Child) {
        
        // Reference database location and listen for updates
        let database = Firestore.firestore()
        database.collection("accounts").document(Auth.auth().currentUser!.uid).collection("profiles").document(Auth.auth().currentUser!.uid + child.getProfileId()).collection("requests").addSnapshotListener { querySnapshot, error in
            guard let documents = querySnapshot?.documents
                else {
                    print("Error fetching documents: \(error!)")
                    return
            }
            
            var requests = [Request]()
            
            // Loop through requests and save to file
            for document in documents {
                let firstApprover = document.get("firstApprover") as? String?
                let requestMessage = document.get("requestMessage") as? String
                let requestStatus = document.get("requestStatus") as? Int
                let requesterId = document.get("requesterId") as? String
                let requesterName = document.get("requesterName") as? String
                let timeStamp = document.get("timeStamp") as? Date
                
                let request = Request(RequesterName: requesterName!, RequesterId: requesterId!, RequestMessage: requestMessage!, TimeStamp: timeStamp!)
                request.setRequestStatus(RequestStatus: requestStatus!)
                
                if firstApprover != nil {
                    request.setFirstApprover(FirstApprover: firstApprover!)
                }
                
                requests.append(request)
            }
            
            saveRequests(Requests: requests)
        }
    }
    
    // Custom method to approve request
    public static func approveRequest(Controller controller: PermissionController, Request request: Request) {
        
        // Reference database
        let database = Firestore.firestore()
        
        // Set request status and first approver
        request.setRequestStatus(RequestStatus: request.getRequestStatus() + 1)
        request.setFirstApprover(FirstApprover: AccountUtils.loadParent()?.getProfileId())
        
        // Map request for upload
        var requests = loadRequests()
        requests.append(request)
        let account = AccountUtils.loadAccount()
        let requestData: [String: Any] = [
            "requesterName": request.getRequesterName(),
            "requesterId": request.getRequesterId(),
            "requestMessage": request.getRequestMessage(),
            "requestStatus": request.getRequestStatus(),
            "timeStamp": request.getTimeStamp(),
            "firstApprover": request.getFirstApprover() as Any
        ]
        
        // Reference database location and attempt to upload to requsting profile
        database.collection("accounts").document(Auth.auth().currentUser!.uid).collection("profiles").document(Auth.auth().currentUser!.uid + request.getRequesterId()).collection("requests").document(request.getRequestId()).setData(requestData) { err in
            if let err = err {
                print("Error writing document: \(err)")
            }
            
            // If successful...
            else {
                
                // Loop through parents and upload then save to file and let user know response was sent successfully
                for parent in (account?.getParents())! {
                    database.collection("accounts").document(Auth.auth().currentUser!.uid).collection("profiles").document(Auth.auth().currentUser!.uid + parent.getProfileId()).collection("incomingRequests").document(request.getRequestId()).setData(requestData) { err in
                        if let err = err {
                            print("Error writing document: \(err)")
                        }
                        
                        else {
                            saveRequests(Requests: requests)
                            
                            let alert = UIAlertController(title: "Success", message: "Request approved", preferredStyle: .alert)
                            alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
                            controller.present(alert, animated: true)
                        }
                    }
                }
            }
        }
    }
    
    // Custom method to deny request
    public static func denyRequest(Controller controller: PermissionController, Request request: Request) {
        
        // Reference databse
        let database = Firestore.firestore()
        
        // Set request status
        request.setRequestStatus(RequestStatus: -1)
        
        // Map request for upload
        var requests = loadRequests()
        requests.append(request)
        let account = AccountUtils.loadAccount()
        let requestData: [String: Any] = [
            "requesterName": request.getRequesterName(),
            "requesterId": request.getRequesterId(),
            "requestMessage": request.getRequestMessage(),
            "requestStatus": request.getRequestStatus(),
            "timeStamp": request.getTimeStamp(),
            "firstApprover": request.getFirstApprover() as Any
        ]
        
        // Reference database location and attempt to upload to requesting profile
        database.collection("accounts").document(Auth.auth().currentUser!.uid).collection("profiles").document(Auth.auth().currentUser!.uid + request.getRequesterId()).collection("requests").document(request.getRequestId()).setData(requestData) { err in
            if let err = err {
                print("Error writing document: \(err)")
            }
               
            // If successful, loop through parents and upload then let user know response was sent successfully
            else {
                for parent in (account?.getParents())! {
                    database.collection("accounts").document(Auth.auth().currentUser!.uid).collection("profiles").document(Auth.auth().currentUser!.uid + parent.getProfileId()).collection("incomingRequests").document(request.getRequestId()).setData(requestData) { err in
                        if let err = err {
                            print("Error writing document: \(err)")
                        }
                            
                        else {
                            saveRequests(Requests: requests)
                            
                            let alert = UIAlertController(title: "Success", message: "Request denied", preferredStyle: .alert)
                            alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
                            controller.present(alert, animated: true)
                        }
                    }
                }
            }
        }
    }
    
    // Custom method to load requests from file
    public static func loadRequests() -> [Request] {
        let fileName = getDocumentsDirectory().appendingPathComponent("requests.fam")
        var requests = [Request]()
        
        do {
            let jsonString = try String(contentsOf: fileName)
            if let jsonData = jsonString.data(using: .utf8) {
                
                do {
                    try requests = JSONDecoder().decode([Request].self, from: jsonData)
                }
                    
                catch {
                    print("Error loading newsfeed")
                }
            }
        }
        
        catch {
            print("Error loading requests from file")
        }
        
        return requests
    }
    
    // Custom method to save requests to file
    public static func saveRequests(Requests requests: [Request]) {
        let saveRequests = requests.sorted(by: {$0.getTimeStamp().compare($1.getTimeStamp()) == .orderedDescending})
        let encodedObject = try? JSONEncoder().encode(saveRequests)
        if let encodedObjectJsonString = String(data: encodedObject!, encoding: .utf8) {
            let fileName = getDocumentsDirectory().appendingPathComponent("requests.fam")
            
            do {
                // Attempt to save JSON string to file
                try encodedObjectJsonString.write(to: fileName, atomically: true, encoding: String.Encoding.utf8)
            }
                
            catch {
                print("Failed to write data to file")
            }
        }
    }
    
    // Custom method to get documents directory
    public static func getDocumentsDirectory() -> URL {
        let paths = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)
        return paths[0]
    }
}
