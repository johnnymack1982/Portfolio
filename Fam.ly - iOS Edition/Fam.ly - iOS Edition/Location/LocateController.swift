//
//  LocateController.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 6/17/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import UIKit
import GoogleMaps

class LocateController: UIViewController {
    
    
    
    // UI Outlets
    @IBOutlet weak var profilePhoto: UIImageView!
    @IBOutlet weak var profileNameDisplay: UILabel!
    @IBOutlet weak var timestampDisplay: UILabel!
    @IBOutlet weak var lastKownLocationDisplay: UILabel!
    @IBOutlet weak var mapView: GMSMapView!
    
    
    
    // Class properties
    var mAccount: Account?
    var mParent: Parent?
    var mChild: Child?
    
    
    
    // System generated methods
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        mAccount = AccountUtils.loadAccount()
        populate()
    }
    
    
    
    // Custom methods
    private func populate() {
        var profileId: String?
        var firstname: String?
        
        if mParent != nil {
            profileId = mParent?.getProfileId()
            firstname = mParent?.getFirstName()
        }
        
        else if mChild != nil {
            profileId = mChild?.getProfileId()
            firstname = mChild?.getFirstName()
        }
        
        AccountUtils.loadProfilePhoto(ProfileId: profileId!, ProfilePhoto: profilePhoto)
        roundImageView()
        
        profileNameDisplay.text = firstname! + " " + (mAccount?.getFamilyName())!
        
        let locationUtils = LocationUtils(Location: nil)
        locationUtils.updateLocationDisplay(Controller: self, TimeStampDisplay: timestampDisplay, LastKnowLocationDisplay: lastKownLocationDisplay, Parent: mParent, Child: mChild)
        
        locationUtils.getProfileLocation(Controller: self, Parent: mParent, Child: mChild, Map: mapView)
    }
    
    func roundImageView() {
        profilePhoto.layer.borderWidth = 1.0
        profilePhoto.layer.masksToBounds = false
        profilePhoto.layer.borderColor = UIColor.white.cgColor
        profilePhoto.layer.cornerRadius = profilePhoto.frame.size.width / 2
        profilePhoto.clipsToBounds = true
    }
    
    
    
    // Action methods
    @IBAction func locateButtonClicked(_ sender: UIButton) {
        let locationUtils = LocationUtils(Location: nil)
        locationUtils.getProfileLocation(Controller: self, Parent: mParent, Child: mChild, Map: mapView)
    }
}
