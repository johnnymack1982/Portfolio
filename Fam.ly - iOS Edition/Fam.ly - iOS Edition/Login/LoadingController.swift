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
        let user = Auth.auth().currentUser
        
        if user != nil {
            DispatchQueue.main.async(){
                let database = Firestore.firestore()
                let documentRef = database.collection("accounts").document(user!.uid)
                documentRef.getDocument { (document, error) in
                    if let document = document, document.exists {
                        
                        database.collection("accounts").document(user!.uid).collection("profiles").getDocuments() { (querySnapshot, err) in
                            if let err = err {
                                print("Error getting documents: \(err)")
                            }
                                
                            else if AccountUtils.loadAccount() != nil {
                                AccountUtils.getUpdatedProfiles()
                                
                                let parent = AccountUtils.loadParent()
                                let child = AccountUtils.loadChild()
                                
                                if parent == nil && child == nil {
                                    self.performSegue(withIdentifier: "LoadingToLogin", sender: nil)
                                }
                                    
                                else {
                                    AccountUtils.saveProfile(Parent: parent, Child: child)
                                    self.performSegue(withIdentifier: "LoadingToNavigation", sender: nil)
                                }
                            }
                        }
                    }
                        
                    else {
                        self.performSegue(withIdentifier: "LoadingToLogin", sender: nil)
                    }
                }
            }
        }
        
        else {
            self.performSegue(withIdentifier: "LoadingToLogin", sender: nil)
        }
    }
}
