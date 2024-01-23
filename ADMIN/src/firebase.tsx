// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getDatabase } from "firebase/database";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
const firebaseConfig = {
  apiKey: "AIzaSyAGhFCh6FaBDt0qNtggA3E-RDbwllXg1T0",
  authDomain: "tropical-forest-8587c.firebaseapp.com",
  databaseURL: "https://tropical-forest-8587c-default-rtdb.firebaseio.com",
  projectId: "tropical-forest-8587c",
  storageBucket: "tropical-forest-8587c.appspot.com",
  messagingSenderId: "328317277513",
  appId: "1:328317277513:web:cc0e563b8ab042ae183698",
  measurementId: "G-BPK2N5J8X4"
};


// Initialize Firebase
const app = initializeApp(firebaseConfig);
export const database = getDatabase(app);