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
        
        postImage.image = nil
        postImageHeight.constant = 0
        
        mParent = AccountUtils.loadParent()
        mChild = AccountUtils.loadChild()
        
        if mParent != nil {
            findButton.isHidden = false
            mFirstName = mParent?.getFirstName()
        }
        
        else if mChild != nil {
            findButton.isHidden = true
            mFirstName = mChild?.getFirstName()
        }
        
        mLocationManager.delegate = self
        
        let authorizationStatus = CLLocationManager.authorizationStatus()
        
        if authorizationStatus == CLAuthorizationStatus.notDetermined {
            mLocationManager.requestAlwaysAuthorization()
        }
        
        else {
            mLocationManager.startUpdatingLocation()
        }
    }
    
    override func viewWillAppear(_ animated: Bool) {
        postImageHeight.constant = 0
        
        let authorizationStatus = CLLocationManager.authorizationStatus()
        
        if authorizationStatus == CLAuthorizationStatus.notDetermined {
            mLocationManager.requestAlwaysAuthorization()
        }
            
        else {
            mLocationManager.startUpdatingLocation()
        }
    }
    
    
    
    // Custom methods
    func takePhoto() {
        cameraPicker =  UIImagePickerController()
        cameraPicker.delegate = self
        cameraPicker.sourceType = .camera
        
        present(cameraPicker, animated: true, completion: nil)
    }
    
    
    
    // Action methods
    @IBAction func buttonClicked(_ sender: UIButton) {
        switch sender.tag {
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
        guard status == .authorizedWhenInUse || status == .authorizedAlways else {
            return
        }

        mLocationManager.startUpdatingLocation()
        
        mapView.isMyLocationEnabled = true
        mapView.settings.myLocationButton = true
    }
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        guard let location = locations.first else {
            return
        }
        
        mLocation = location
        
        let locationUtils = LocationUtils(Location: location)
        locationUtils.updateLocation(Parent: mParent, Child: mChild)
        
        mapView.camera = GMSCameraPosition(target: location.coordinate, zoom: 15, bearing: 0, viewingAngle: 0)
        
        let geocoder = GMSGeocoder()
        geocoder.reverseGeocodeCoordinate(location.coordinate) { (response, error) in
            guard let address = response?.firstResult(), let lines = address.lines else {
                return
            }
            
            let marker = GMSMarker()
            marker.position = location.coordinate
            marker.title = self.mFirstName! + " " + (AccountUtils.loadAccount()?.getFamilyName())!
            marker.snippet = lines.joined(separator: " ")
            marker.map = self.mapView
        }
    }
}



extension CheckinController : ImagePickerDelegate {
    
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
