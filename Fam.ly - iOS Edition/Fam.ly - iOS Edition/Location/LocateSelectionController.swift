//
//  LocateSelectionController.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 6/17/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import UIKit

class LocateSelectionController: UIViewController, UITableViewDataSource, UITableViewDelegate {
    
    
    
    // UI Outlets
    @IBOutlet weak var profilesTable: UITableView!
    
    
    
    // Class properties
    var mAccount: Account?
    
    var mParent: Parent?
    var mChild: Child?
    
    var mPosition = 0
    
    
    
    // System generated methods
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        
        // Show top navigation bar
        self.navigationController!.isNavigationBarHidden = false
        
        // Load account
        mAccount = AccountUtils.loadAccount()
        
        // Set tableview source and delegate
        profilesTable.dataSource = self
        profilesTable.delegate = self
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.destination is LocateController {
            
            // Send selected profile to destination
            let destination = segue.destination as? LocateController
            
            if mParent != nil {
                destination?.mParent = mParent
            }
                
            else if mChild != nil {
                destination?.mChild = mChild
            }
        }
    }
    
    
    
    // Tableview methods
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        // Return number of profiles on account
        return (mAccount?.getParents().count)! + (mAccount?.getChildren().count)!
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        // Reference cell
        let cell = profilesTable.dequeueReusableCell(withIdentifier: "profileReuseIdentifier") as! ProfileListCell
        
        // Load profiles
        let parents = mAccount?.getParents()
        let children = mAccount?.getChildren()
        
        // Populate profile info
        if indexPath.row < parents!.count {
            let parent = parents![indexPath.row]
            let profileId = parent.getProfileId()
            cell.profileNameDisplay.text = parent.getFirstName() + " " + (mAccount?.getFamilyName())!
            
            AccountUtils.loadProfilePhoto(ProfileId: profileId, ProfilePhoto: cell.profilePhoto)
        }
            
        // Populate profile info
        else {
            let child = children![indexPath.row - parents!.count]
            let profileId = child.getProfileId()
            cell.profileNameDisplay.text = child.getFirstName() + " " + (mAccount?.getFamilyName())!
            
            AccountUtils.loadProfilePhoto(ProfileId: profileId, ProfilePhoto: cell.profilePhoto)
        }
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        // Load profiles
        let parents = mAccount?.getParents()
        let children = mAccount?.getChildren()
        
        // Launch profile location activity
        if indexPath.row < parents!.count {
            mParent = parents![indexPath.row]
            mChild = nil
            
            mPosition = indexPath.row
            
            performSegue(withIdentifier: "LocateSelectionToLocate", sender: nil)
        }
            
        else {
            mChild = children![indexPath.row - parents!.count]
            mParent = nil
            
            mPosition = indexPath.row - parents!.count
            
            performSegue(withIdentifier: "LocateSelectionToLocate", sender: nil)
        }
    }
}
