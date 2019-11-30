There are three projects in this repository. 
1.	Sender android app
2.	Receiver android app
3.	ASP.NET core APIs(for backend)

Sender app will register the employee information and save it in the JSON file format in the internal storage of the mobile. After registration, it will make a POST API call through the volley library. Mobile data will sync to the server (azure app services and cosmos DB).
Whereas the Receiver app will get the information from the server through GET API call from the same server and DB.

Compilation Instructions-
While compiling the code for android apps, Select the configuration of the sender app or receiver app accordingly.
ASP.NET core web API is added in the AzureBackend folder & COSMOS DB is used for the database. It includes two API get and post for both apps.
