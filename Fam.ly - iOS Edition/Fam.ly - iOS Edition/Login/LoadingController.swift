//
//  LoadingController.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 6/18/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import UIKit
import FirebaseAuth
import FirebaseFirestore

class LoadingController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        
        // Check for logged in user
        let user = Auth.auth().currentUser
        
        // If a user exists...
        if user != nil {
            DispatchQueue.main.async(){
                
                // Reference account database location and attempt to download
                let database = Firestore.firestore()
                let documentRef = database.collection("accounts").document(user!.uid)
                documentRef.getDocument { (document, error) in
                    
                    // If document exists...
                    if let document = document, document.exists {
                        
                        // Refrence profile database location and listen for profile updates
                        database.collection("accounts").document(user!.uid).collection("profiles").getDocuments() { (querySnapshot, err) in
                            if let err = err {
                                print("Error getting documents: \(err)")
                            }
                            
                            // If successful...
                            else if AccountUtils.loadAccount() != nil {
                                
                                // Fetch updated profiles
                                AccountUtils.getUpdatedProfiles()
                                
                                // Load profile from file
                                let parent = AccountUtils.loadParent()
                                let child = AccountUtils.loadChild()
                                
                                // If no profile exists, launch login activity
                                if parent == nil && child == nil {
                                    self.performSegue(withIdentifier: "LoadingToLogin", sender: nil)
                                }
                                    
                                // Otherwise, save profile to file and launch navigation activity
                                else {
                                    AccountUtils.saveProfile(Parent: parent, Child: child)
                                    self.performSegue(withIdentifier: "LoadingToNavigation", sender: nil)
                                }
                            }
                        }
                    }
                        
                    // If login is unsuccessful, launch login activity
                    else {
                        self.performSegue(withIdentifier: "LoadingToLogin", sender: nil)
                    }
                }
            }
        }
        
        // If login is unsuccessful, launch login activity
        else {
            self.performSegue(withIdentifier: "LoadingToLogin", sender: nil)
        }
    }
}
