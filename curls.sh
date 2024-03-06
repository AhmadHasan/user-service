#####################
#     Swagger UI    #
#####################

http://localhost:8086/api/v0/swagger-ui/index.html

#####################
#     Health Check  #
#####################

curl -i -X GET http://localhost:8086/api/v0/health

#####################
#  Users            #
#####################

# Create User

curl -X PUT http://localhost:8086/api/v0/user -H "Content-Type: application/json" -d '{ "countryCodes": ["DE"], "languageCodes": ["de"] }'
