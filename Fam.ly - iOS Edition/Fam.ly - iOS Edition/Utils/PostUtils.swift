//
//  File.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 6/3/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import Foundation
import FirebaseAuth
import FirebaseFirestore
import FirebaseStorage
import UIKit

public class PostUtils {
    
    
    
    // Custom methods
    public static func createPost(Post post: Post, Controller controller: UIViewController, Photo photo: UIImage?) {
        let database = Firestore.firestore()
        
        let postData: [String: Any] = [
            "postMessage": post.getPostMessage(),
            "timeStamp": post.getTimeStamp(),
            "posterId": post.getPosterId(),
            "posterName": post.getPosterName(),
            "hasImage": post.getHasImage()
        ]
        
        database.collection("accounts").document(Auth.auth().currentUser!.uid).collection("newsfeed").document(post.getPostId()).setData(postData) { err in
            if let err = err {
                print("Error writing document: \(err)")
            }
            
            else {
                database.collection("accounts").document(Auth.auth().currentUser!.uid).collection("profiles").document(Auth.auth().currentUser!.uid + post.getPosterId()).collection("posts").document(post.getPostId()).setData(postData) { err in
                    if let err = err {
                        print("Error writing document: \(err)")
                    }
                    
                    else {
                        if photo != nil {
                            let storage = Storage.storage()
                            let storageRef = storage.reference()
                            let photoRef = storageRef.child("photos/" + Auth.auth().currentUser!.uid + post.getPostId() + ".jpg")
                            
                            let imageData = photo!.jpegData(compressionQuality: 0.5)
                            
                            photoRef.putData(imageData!)
                        }
                        
                        let alert = UIAlertController(title: "Success", message: "Post created", preferredStyle: .alert)
                        alert.addAction(UIAlertAction(title: "OK", style: .cancel, handler: nil))
                        controller.present(alert, animated: true)
                    }
                }
            }
        }
    }
    
    public static func listenForNews() {
        let database = Firestore.firestore()
        
        let sevenDaysAgo = Calendar.current.date(byAdding: .day, value: -7, to: Date())
        
        database.collection("accounts").document(Auth.auth().currentUser!.uid).collection("newsfeed").whereField("timeStamp", isGreaterThanOrEqualTo: sevenDaysAgo!).addSnapshotListener { querySnapshot, error in
            guard let documents = querySnapshot?.documents else {
                print("Error fetching documents: \(error!)")
                return
            }
            
            var posts = [Post]()
            
            for document in documents {
                let postMessage = document.get("postMessage") as? String
                let timeStamp = document.get("timeStamp") as? Date
                let posterId = document.get("posterId") as? String
                let posterName = document.get("posterName") as? String
                let hasImage = document.get("hasImage") as? Bool
                
                let post = Post(PostMessage: postMessage!, TimeStamp: timeStamp!, PosterId: posterId!, PosterName: posterName!, HasImage: hasImage!)
                
                posts.append(post)
                posts.reverse()
            }
            
            let encodedObject = try? JSONEncoder().encode(posts)
            
            if let encodedObjectJsonString = String(data: encodedObject!, encoding: .utf8) {
                
                
                let fileName = getDocumentsDirectory().appendingPathComponent("newsfeed.fam")
                
                do {
                    // Attempt to save JSON string to file
                    try encodedObjectJsonString.write(to: fileName, atomically: true, encoding: String.Encoding.utf8)
                }
                    
                catch {
                    print("Failed to write data to file")
                }
            }
        }
    }
    
    public static func getDocumentsDirectory() -> URL {
        let paths = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)
        return paths[0]
    }
    
    public static func loadNewsfeed() -> [Post] {
        let fileName = getDocumentsDirectory().appendingPathComponent("newsfeed.fam")
        
        var posts = [Post]()
        
        do {
            let jsonString = try String(contentsOf: fileName)
            
            if let jsonData = jsonString.data(using: .utf8) {
                
                do {
                    try posts = JSONDecoder().decode([Post].self, from: jsonData)
                }
                
                catch {
                    print("Error loading newsfeed")
                }
            }
        }
            
        catch {
            print("Error loading account from file")
        }
        
        return posts
    }
    
    public static func loadPostImage(View postImage: UIImageView, PostId postId: String, ImageHeight imageHeight: NSLayoutConstraint) {
        let storage = Storage.storage()
        let storageRef = storage.reference()
        let photoRef = storageRef.child("photos/" + Auth.auth().currentUser!.uid + postId + ".jpg")
        
        photoRef.getData(maxSize: 1 * 1024 * 1024) { data, error in
            if error != nil {
                postImage.image = nil
            }
            
            else {
                let image = UIImage(data: data!)
                postImage.image = image
            }
        }
    }
    
    public static func deletePost(PostId postId: String, PosterId posterId: String, Controller controller: UIViewController) {
        let database = Firestore.firestore()
        
        let storage = Storage.storage()
        let storageRef = storage.reference()
        let photoRef = storageRef.child("photos/" + Auth.auth().currentUser!.uid + postId + ".jpg")
        
        database.collection("accounts").document(Auth.auth().currentUser!.uid).collection("newsfeed").document(postId).delete() { err in
            if let err = err {
                print("Error removing document: \(err)")
            }
            
            else {
                database.collection("accounts").document(Auth.auth().currentUser!.uid).collection("profiles").document(Auth.auth().currentUser!.uid + posterId).collection("posts").document(postId).delete() { err in
                    if let err = err {
                        print("Error removing document: \(err)")
                    }
                    
                    else {
                        photoRef.delete(completion: nil)
                        
                        let alert = UIAlertController(title: "Post Deleted", message: "This post has been successfully removed", preferredStyle: .alert)
                        alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
                        controller.present(alert, animated: true)
                    }
                }
            }
        }
    }
    
    public static func updatePost(Post post: Post, Posts posts: [Post], Controller controller: UIViewController) {
        let encodedObject = try? JSONEncoder().encode(posts)
        
        if let encodedObjectJsonString = String(data: encodedObject!, encoding: .utf8) {
            
            
            let fileName = getDocumentsDirectory().appendingPathComponent("posts.fam")
            
            do {
                // Attempt to save JSON string to file
                try encodedObjectJsonString.write(to: fileName, atomically: true, encoding: String.Encoding.utf8)
            }
                
            catch {
                print("Failed to write data to file")
            }
        }
        
        let postData: [String: Any] = [
            "postMessage": post.getPostMessage(),
            "timeStamp": post.getTimeStamp(),
            "posterId": post.getPosterId(),
            "posterName": post.getPosterName(),
            "hasImage": post.getHasImage()
        ]
        
        let databse = Firestore.firestore()
        
        databse.collection("accounts").document(Auth.auth().currentUser!.uid).collection("newsfeed").document(post.getPostId()).setData(postData) { err in
            if let err = err {
                print("Error writing document: \(err)")
            }
            
            else {
                databse.collection("accounts").document(Auth.auth().currentUser!.uid).collection("profiles").document(Auth.auth().currentUser!.uid + post.getPosterId()).collection("posts").document(post.getPostId()).setData(postData) { err in
                    if let err = err {
                        print("Error writing document: \(err)")
                    }
                    
                    else {
                        let alert = UIAlertController(title: "Post Updated", message: "Your post has been successfully updated", preferredStyle: .alert)
                        
                        alert.addAction(UIAlertAction(title: "OK", style: .default, handler: { (UIAlertAction) in
                            controller.performSegue(withIdentifier: "unwindToNewsfeed", sender: controller)
                        }))
                        
                        controller.present(alert, animated: true)
                    }
                }
            }
        }
    }
}
