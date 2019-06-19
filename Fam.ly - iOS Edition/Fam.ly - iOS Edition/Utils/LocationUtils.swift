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
    public func updateLocation(Parent parent: Parent?, Child child: Child?) {
        var profileId: String?
        
        if parent != nil {
            profileId = parent?.getProfileId()
        }
        
        else if child != nil {
            profileId = child?.getProfileId()
        }
        
        let database = Firestore.firestore()
        
        let date = Date()
        
        let checkinData: [String: Any] = [
            "latitude": mLatitude ?? 0,
            "longitude": mLongitude ?? 0,
            "checkinTimestamp": date
        ]
        
        database.collection("accounts").document(Auth.auth().currentUser!.uid).collection("profiles").document(Auth.auth().currentUser!.uid + profileId!).collection("location").document("location").setData(checkinData) { err in
            if let err = err {
                print("Error writing document: \(err)")
            }
            
            else {
                print("Location updated")
            }
        }
    }
    
    public func getProfileLocation(Controller controller: UIViewController, Parent parent: Parent?, Child child: Child?, Map map: GMSMapView) {
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
        
        let database = Firestore.firestore()
        
        let documentRef = database.collection("accounts").document(Auth.auth().currentUser!.uid).collection("profiles").document(Auth.auth().currentUser!.uid + profileId!).collection("location").document("location")
        
        documentRef.getDocument { (document, error) in
            if let document = document, document.exists {
                self.mLatitude = document.get("latitude") as? Double
                self.mLongitude = document.get("longitude") as? Double
                
                let location = CLLocation(latitude: self.mLatitude!, longitude: self.mLongitude!)
                
                map.camera = GMSCameraPosition(target: location.coordinate, zoom: 15, bearing: 0, viewingAngle: 0)
                
                let geocoder = GMSGeocoder()
                geocoder.reverseGeocodeCoordinate(location.coordinate) { (response, error) in
                    guard let address = response?.firstResult(), let lines = address.lines else {
                        return
                    }
                    
                    let marker = GMSMarker()
                    marker.position = location.coordinate
                    marker.title = firstName! + " " + (AccountUtils.loadAccount()?.getFamilyName())!
                    marker.snippet = lines.joined(separator: " ")
                    marker.map = map
                }
            }
            
            else {
                let alert = UIAlertController(title: "Unable to Locate", message: "Location information for " + firstName! + " is unavailable.", preferredStyle: .alert)
                
                alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
                
                controller.present(alert, animated: true)
            }
        }
    }
    
    public func checkIn(Controller controller: UIViewController, Parent parent: Parent?, Child child: Child?, Message message: String?, Image image: UIImage?) {
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
        
        let database = Firestore.firestore()
        
        let date = Date()
        
        let checkinData: [String: Any] = [
            "latitude": mLatitude!,
            "longitude": mLongitude!,
            "checkinTimestamp": date
        ]
        
        database.collection("accounts").document(Auth.auth().currentUser!.uid).collection("profiles").document(Auth.auth().currentUser!.uid + profileId!).collection("location").document("location").setData(checkinData) { err in
            if let err = err {
                print("Error writing document: \(err)")
                
                let alert = UIAlertController(title: "Uh Oh!", message: "There was a problem checking in.", preferredStyle: .alert)
                
                alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
                
                controller.present(alert, animated: true)
            }
                
            else {
                let location = CLLocation(latitude: self.mLatitude!, longitude: self.mLongitude!)
                
                let geocoder = GMSGeocoder()
                geocoder.reverseGeocodeCoordinate(location.coordinate) { (response, error) in
                    guard let address = response?.firstResult(), let lines = address.lines else {
                        return
                    }
                    
                    var hasImage = false
                    
                    if image != nil {
                        hasImage = true
                    }
                    
                    else {
                        hasImage = false
                    }
                    
                    if message == nil || message?.trimmingCharacters(in: .whitespacesAndNewlines) == "" {
                        let postMessage = firstName! + " checked in at " + lines.joined(separator: " ")
                        
                        let post = Post(PostMessage: postMessage, TimeStamp: date, PosterId: profileId!, PosterName: firstName!, HasImage: hasImage)
                        PostUtils.createPost(Post: post, Controller: controller, Photo: image)
                    }
                    
                    else {
                        let postMessage = firstName! + " checked in at " + address.lines!.joined(separator: " ") + " and says '" + message! + "'"
                        
                        let post = Post(PostMessage: postMessage, TimeStamp: date, PosterId: profileId!, PosterName: firstName!, HasImage: hasImage)
                        PostUtils.createPost(Post: post, Controller: controller, Photo: image)
                    }
                }
            }
        }
    }
    
    public func updateLocationDisplay(Controller controller: UIViewController, TimeStampDisplay timeStampDisplay: UILabel, LastKnowLocationDisplay lastKnownLocationDisplay: UILabel, Parent parent: Parent?, Child child: Child?) {
        
        timeStampDisplay.text = ""
        lastKnownLocationDisplay.text = ""
        
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
        
        let database = Firestore.firestore()
        
        let documentRef = database.collection("accounts").document(Auth.auth().currentUser!.uid).collection("profiles").document(Auth.auth().currentUser!.uid + profileId!).collection("location").document("location")
        
        documentRef.getDocument { (document, error) in
            if let document = document, document.exists {
                self.mLatitude = document.get("latitude") as? Double
                self.mLongitude = document.get("longitude") as? Double
                let timeStamp = document.get("checkinTimestamp") as? Date
                
                let location = CLLocation(latitude: self.mLatitude!, longitude: self.mLongitude!)
                
                let geocoder = GMSGeocoder()
                geocoder.reverseGeocodeCoordinate(location.coordinate) { (response, error) in
                    guard let address = response?.firstResult(), let lines = address.lines else {
                        return
                    }
                    
                    let dateFormatter = DateFormatter()
                    dateFormatter.dateFormat = "MMMM dd, yyyy @ hh:mm a"
                    let dateString = dateFormatter.string(from: timeStamp!)
                    
                    timeStampDisplay.text = dateString
                    lastKnownLocationDisplay.text = lines.joined(separator: " ")
                }
            }
            
            else {
                lastKnownLocationDisplay.text = "Unknown Location"
                
                let alert = UIAlertController(title: "Unable to Locate", message: "Location information for " + firstName! + " is unavailable.", preferredStyle: .alert)
                
                alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
                
                controller.present(alert, animated: true)
            }
        }
    }
}
