//
//  LocationUtils.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 6/17/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import Foundation
import GoogleMaps
import FirebaseFirestore
import FirebaseAuth

class LocationUtils {
    
    
    
    // Class properties
    let mLocationManager = CLLocationManager()
    
    var mLatitude: Double?
    var mLongitude: Double?
    
    
    
    // Connstructor
    init(Location location: CLLocation?) {
        if location != nil {
            self.mLatitude = location!.coordinate.latitude
            self.mLongitude = location!.coordinate.longitude
        }
    }
    
    
    
    // Custom methods
    // Custom method to update current user's location
    public func updateLocation(Parent parent: Parent?, Child child: Child?) {
        
        // Reference profile id
        var profileId: String?
        
        if parent != nil {
            profileId = parent?.getProfileId()
        }
        
        else if child != nil {
            profileId = child?.getProfileId()
        }
        
        // Reference database
        let database = Firestore.firestore()
        
        // Reference current date
        let date = Date()
        
        // Map checkin for upload
        let checkinData: [String: Any] = [
            "latitude": mLatitude ?? 0,
            "longitude": mLongitude ?? 0,
            "checkinTimestamp": date
        ]
        
        // Reference database location and attempt to upload
        database.collection("accounts").document(Auth.auth().currentUser!.uid).collection("profiles").document(Auth.auth().currentUser!.uid + profileId!).collection("location").document("location").setData(checkinData) { err in
            if let err = err {
                print("Error writing document: \(err)")
            }
            
            else {
                print("Location updated")
            }
        }
    }
    
    // Custom method to get profile's last known location
    public func getProfileLocation(Controller controller: UIViewController, Parent parent: Parent?, Child child: Child?, Map map: GMSMapView) {
        
        // Reference profile id and name
        var profileId: String?
        var firstName: String?
        
        if parent != nil {
            profileId = parent?.getProfileId()
            firstName = parent?.getFirstName()
        }
        
        else if child != nil {
            profileId = child?.getProfileId()
            firstName = child?.getFirstName()
        }
        
        // Reference database location and attempt to download
        let database = Firestore.firestore()
        let documentRef = database.collection("accounts").document(Auth.auth().currentUser!.uid).collection("profiles").document(Auth.auth().currentUser!.uid + profileId!).collection("location").document("location")
        
        documentRef.getDocument { (document, error) in
            
            // If documennt exists...
            if let document = document, document.exists {
                
                // Get latitude and longitude
                self.mLatitude = document.get("latitude") as? Double
                self.mLongitude = document.get("longitude") as? Double
                
                // Convert to location data
                let location = CLLocation(latitude: self.mLatitude!, longitude: self.mLongitude!)
                
                // Show location on map
                map.camera = GMSCameraPosition(target: location.coordinate, zoom: 15, bearing: 0, viewingAngle: 0)
                
                // Conver location to readable address
                let geocoder = GMSGeocoder()
                geocoder.reverseGeocodeCoordinate(location.coordinate) { (response, error) in
                    guard let address = response?.firstResult(), let lines = address.lines else {
                        return
                    }
                    
                    // Add marker to map
                    let marker = GMSMarker()
                    marker.position = location.coordinate
                    marker.title = firstName! + " " + (AccountUtils.loadAccount()?.getFamilyName())!
                    marker.snippet = lines.joined(separator: " ")
                    marker.map = map
                }
            }
            
            // If there was a problem, let the user know
            else {
                let alert = UIAlertController(title: "Unable to Locate", message: "Location information for " + firstName! + " is unavailable.", preferredStyle: .alert)
                alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
                controller.present(alert, animated: true)
            }
        }
    }
    
    // Custom method to create new checkin
    public func checkIn(Controller controller: UIViewController, Parent parent: Parent?, Child child: Child?, Message message: String?, Image image: UIImage?) {
        
        // Reference profile id and name
        var profileId: String?
        var firstName: String?
        
        if parent != nil {
            profileId = parent?.getProfileId()
            firstName = parent?.getFirstName()
        }
        
        else if child != nil {
            profileId = child?.getProfileId()
            firstName = child?.getFirstName()
        }
        
        // Reference database
        let database = Firestore.firestore()
        
        // Reference current date
        let date = Date()
        
        // Map checkin for upload
        let checkinData: [String: Any] = [
            "latitude": mLatitude!,
            "longitude": mLongitude!,
            "checkinTimestamp": date
        ]
        
        // Reference database location and attempt to upload
        database.collection("accounts").document(Auth.auth().currentUser!.uid).collection("profiles").document(Auth.auth().currentUser!.uid + profileId!).collection("location").document("location").setData(checkinData) { err in
            
            // If there was a problem, let the user know
            if let err = err {
                print("Error writing document: \(err)")
                
                let alert = UIAlertController(title: "Uh Oh!", message: "There was a problem checking in.", preferredStyle: .alert)
                alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
                controller.present(alert, animated: true)
            }
                
            // If successful...
            else {
                
                // Create location data
                let location = CLLocation(latitude: self.mLatitude!, longitude: self.mLongitude!)
                
                // Convert location to readable address
                let geocoder = GMSGeocoder()
                geocoder.reverseGeocodeCoordinate(location.coordinate) { (response, error) in
                    guard let address = response?.firstResult(), let lines = address.lines else {
                        return
                    }
                    
                    // Indicate whether or not post includes a photo
                    var hasImage = false
                    
                    if image != nil {
                        hasImage = true
                    }
                    
                    else {
                        hasImage = false
                    }
                    
                    // If post has a message include it and create newsfeed post
                    if message == nil || message?.trimmingCharacters(in: .whitespacesAndNewlines) == "" {
                        let postMessage = firstName! + " checked in at " + lines.joined(separator: " ")
                        
                        let post = Post(PostMessage: postMessage, TimeStamp: date, PosterId: profileId!, PosterName: firstName!, HasImage: hasImage)
                        PostUtils.createPost(Post: post, Controller: controller, Photo: image)
                    }
                    
                    // Otherwise, create post without a message
                    else {
                        let postMessage = firstName! + " checked in at " + address.lines!.joined(separator: " ") + " and says '" + message! + "'"
                        
                        let post = Post(PostMessage: postMessage, TimeStamp: date, PosterId: profileId!, PosterName: firstName!, HasImage: hasImage)
                        PostUtils.createPost(Post: post, Controller: controller, Photo: image)
                    }
                }
            }
        }
    }
    
    // Custom method to update location display for current profile
    public func updateLocationDisplay(Controller controller: UIViewController, TimeStampDisplay timeStampDisplay: UILabel, LastKnowLocationDisplay lastKnownLocationDisplay: UILabel, Parent parent: Parent?, Child child: Child?) {
        
        // Clear UI elements
        timeStampDisplay.text = ""
        lastKnownLocationDisplay.text = ""
        
        // Reference profile id and name
        var profileId: String?
        var firstName: String?
        
        if parent != nil {
            profileId = parent?.getProfileId()
            firstName = parent?.getFirstName()
        }
        
        else if child != nil {
            profileId = child?.getProfileId()
            firstName = child?.getFirstName()
        }
        
        // Reference database location and attempt to download
        let database = Firestore.firestore()
        let documentRef = database.collection("accounts").document(Auth.auth().currentUser!.uid).collection("profiles").document(Auth.auth().currentUser!.uid + profileId!).collection("location").document("location")
        
        documentRef.getDocument { (document, error) in
            
            // If successful...
            if let document = document, document.exists {
                
                // Reference post data
                self.mLatitude = document.get("latitude") as? Double
                self.mLongitude = document.get("longitude") as? Double
                let timeStamp = document.get("checkinTimestamp") as? Date
                
                // Create location
                let location = CLLocation(latitude: self.mLatitude!, longitude: self.mLongitude!)
                
                // Convert location to readable address
                let geocoder = GMSGeocoder()
                geocoder.reverseGeocodeCoordinate(location.coordinate) { (response, error) in
                    guard let address = response?.firstResult(), let lines = address.lines else {
                        return
                    }
                    
                    // Display timestamp
                    let dateFormatter = DateFormatter()
                    dateFormatter.dateFormat = "MMMM dd, yyyy @ hh:mm a"
                    let dateString = dateFormatter.string(from: timeStamp!)
                    
                    // Display address
                    timeStampDisplay.text = dateString
                    lastKnownLocationDisplay.text = lines.joined(separator: " ")
                }
            }
            
            // If there was a problem, let the user know
            else {
                lastKnownLocationDisplay.text = "Unknown Location"
                let alert = UIAlertController(title: "Unable to Locate", message: "Location information for " + firstName! + " is unavailable.", preferredStyle: .alert)
                alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
                controller.present(alert, animated: true)
            }
        }
    }
}
