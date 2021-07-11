# Backend for DARA (Data Access Request Analysis)
Visit our website at https://dara-tuberlin.netlify.app/ for more Information about us, our motivation or the Project in general.\
This README will give a quick glance into how the backend was set up, explain what we do with your data and how anyone could contribute to help.
# Current State of the backend
The backend runs as a Springboot application on a paid google compute engine, our SQL Databases runs on the same machine through Docker Containers.\
We started analyzing different parts of the Data we got from companies such as Google, Facebook, Instagram, Amazon and LinkedIn.\
For advertisement category analysis we send different keywords or categories to a Google Text Analysis API to make Data Comparable between different companies.\
Location analysis has also been started through the OpenStreetMap API.
# Data Handling
To make sure that we aren't part of the Problem we want to make people aware of, any Data uploaded by a person gets anonymized through a randomly generated ID and a Secret.\
This guarantees that even if somehow non authorized people could acces our Database there is nothing that tracks back to you as a user.\
Thats also why we decided to not analyze files containing your personal information. 
Any Data uploaded to our Database gets automatically deleted after 1 week or instantly if the user requests us to do so.
# Contribution
If you want to help with this project
the easiest way for you to contribute is to download our Extension either through the Firefox Extension Store 
or follow the documentation to our Frontend at https://github.com/ProgPrak21/frontend.
Within our Extension you can find a list with companies for which an automatic request is already established.
For any of those you can write your own Controller (following the Naming Convention of /data/{platform name}/advertisement would be appreciated).
Different File Types are already present within other Controllers so taking inspiration from there might save you some time.
.csv files are already read and analyzed in the LinkedIn and Amazon Controller, .json files are analyzed in the Facebook and Instagram Controller.\
Help on anything else is greatly appreciated as well you can find other parts of this project here:\
https://github.com/ProgPrak21/frontend \
https://github.com/ProgPrak21/chromeExtension 
