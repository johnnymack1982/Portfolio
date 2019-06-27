//
//  CheckinController.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 5/22/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import UIKit
import GoogleMaps

class CheckinController: UIViewController, UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    
    
    
    // UI Outlets
    @IBOutlet weak var findButton: UIButton!
    @IBOutlet weak var mapView: GMSMapView!
    @IBOutlet weak var checkinInput: UITextField!
    @IBOutlet weak var postImage: UIImageView!
    @IBOutlet weak var postImageHeight: NSLayoutConstraint!
    @IBOutlet weak var postImageView: UIImageView!
    
    
    
    // Class properties
    var cameraPicker: UIImagePickerController!
    
    private let mLocationManager = CLLocationManager()
    
    var mParent: Parent?
    var mChild: Child?
    
    var mFirstName: String?
    
    var mLocation: CLLocation?
    
    
    
    // System generated methods
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Hide image view by default
        postImage.image = nil
        postImageHeight.constant = 0
        
        // Load profile
        mParent = AccountUtils.loadParent()
        mChild = AccountUtils.loadChild()
        
        // If profile is a parent, show locate button and get profile name
        if mParent != nil {
            findButton.isHidden = false
            mFirstName = mParent?.getFirstName()
        }
        
        // If profile is a child, hide locate button and get profile name
        else if mChild != nil {
            findButton.isHidden = true
            mFirstName = mChild?.getFirstName()
        }
        
        // Set location manager delegate
        mLocationManager.delegate = self
        
        // Request location permission if necessary
        let authorizationStatus = CLLocationManager.authorizationStatus()
        if authorizationStatus == CLAuthorizationStatus.notDetermined {
            mLocationManager.requestAlwaysAuthorization()
        }
        
        // Begin updating location
        else {
            mLocationManager.startUpdatingLocation()
        }
    }
    
    override func viewWillAppear(_ animated: Bool) {
        
        // Hide image view by default
        postImageHeight.constant = 0
        
        // Request location permission if necessary
        let authorizationStatus = CLLocationManager.authorizationStatus()
        if authorizationStatus == CLAuthorizationStatus.notDetermined {
            mLocationManager.requestAlwaysAuthorization()
        }
          
        // Begin updating location
        else {
            mLocationManager.startUpdatingLocation()
        }
    }
    
    
    
    // Custom methods
    // Custom method to get photo from camera
    func takePhoto() {
        cameraPicker =  UIImagePickerController()
        cameraPicker.delegate = self
        cameraPicker.sourceType = .camera
        
        present(cameraPicker, animated: true, completion: nil)
    }
    
    
    
    // Action methods
    @IBAction func buttonClicked(_ sender: UIButton) {
        switch sender.tag {
            
        // If user clicked checkin button, create checkin
        case 0:
            let locationUtils = LocationUtils(Location: mLocation!)
            locationUtils.checkIn(Controller: self, Parent: mParent, Child: mChild, Message: checkinInput.text, Image: postImage.image)
            
        case 1:
            print("Locate button clicked")
            
        default:
            print("Invalid button")
        }
    }
}




extension CheckinController: CLLocationManagerDelegate {
    func locationManager(_ manager: CLLocationManager, didChangeAuthorization status: CLAuthorizationStatus) {
        
        // Check for required permission
        guard status == .authorizedWhenInUse || status == .authorizedAlways else {
            return
        }

        // Start updatig location
        mLocationManager.startUpdatingLocation()
        mapView.isMyLocationEnabled = true
        mapView.settings.myLocationButton = true
    }
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        
        // Reference location
        guard let location = locations.first else {
            return
        }
        
        mLocation = location
        
        // Update profile location and show on map
        let locationUtils = LocationUtils(Location: location)
        locationUtils.updateLocation(Parent: mParent, Child: mChild)
        mapView.camera = GMSCameraPosition(target: location.coordinate, zoom: 15, bearing: 0, viewingAngle: 0)
        
        // Extract address from location
        let geocoder = GMSGeocoder()
        geocoder.reverseGeocodeCoordinate(location.coordinate) { (response, error) in
            guard let address = response?.firstResult(), let lines = address.lines else {
                return
            }
            
            // Add location marker to map
            let marker = GMSMarker()
            marker.position = location.coordinate
            marker.title = self.mFirstName! + " " + (AccountUtils.loadAccount()?.getFamilyName())!
            marker.snippet = lines.joined(separator: " ")
            marker.map = self.mapView
        }
    }
}



extension CheckinController : ImagePickerDelegate {
    
    // Update UI with selected image
    public func didSelect(image: UIImage?) {
        self.postImage.image = image
        
        if image != nil {
            self.postImageHeight.constant = 300
            postImageView.isHidden = false
        }
            
        else {
            self.postImageHeight.constant = 300
            self.postImage.image = nil
        }
    }
}
