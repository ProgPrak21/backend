# backend for DARA (Data Access Request Analysis)
visit our website at https://dara-tuberlin.netlify.app/ for more Information about us, our motivation or the Project in general\
this README will give a quick glance into how the backend was set up, explain what we do with your data and how anyone could contribute to help.
# Current State of the backend
The backend runs as a Springboot application on a paid google compute engine, our SQL Databases run on the same machine through Docker Containers\
we started analyzing different parts of the Data we got from companies such as Google, Facebook, Instagram, Amazon, LinkedIn\
for advertisement Category analysis we send different keywords or categories to a Google Text Analysis API to make Data Comparable between different companies\
Location analysis has also been started through the OpenStreetMap API.
# Data Handling
to make sure that we aren't part of the Problem we want to make people aware of, any Data uploaded by a person gets anonymized through a randomly generated ID and a Secret.\
this guarantees that even if somehow non authorized people could acces our Database there is nothing that tracks back to you as a user.\
thats also why we decided to not analyze files containing your personal information. 
any Data uploaded to our Database gets automatically deleted after 1 week or instantly if the user requests us to do so.
# Contribution
If you want to help with this project
the easiest way for you to contribute is to download our Extension either through the Firefox Extension Store 
or follow the documentation to our Frontend at https://github.com/ProgPrak21/frontend
Within our Extension you can find a list with companies for which an automatic request is already established
for any of those you can write your own Controller (following the Naming Convention of /data/{platform name}/advertisement would be appreciated)
different File Types are already present within other Controllers so taking Inspiration from there might save you some Time
.csv files are allready read and analyzed in the LinkedIn and Amazon Controller, .json files are analyzed in the Facebook and Instagram Controller\
help on anything else is greatly appreciated as well you can find other parts of this project here:\
https://github.com/ProgPrak21/frontend \
https://github.com/ProgPrak21/chromeExtension 
