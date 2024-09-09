#####################
#     Swagger UI    #
#####################

http://localhost:8086/api/user/v0/swagger-ui.html

#####################
#     Health Check  #
#####################

curl -i -X GET http://localhost:8086/api/user/v0/health

#####################
#  Users            #
#####################

# Create User

curl -X PUT http://localhost:8086/api/user/v0/user -H "Content-Type: application/json" -d '{ "countryCodes": ["DE"], "languageCodes": ["de"] }'

# addUserCountry Endpoint
curl -X POST "http://localhost:8086/api/user/v0/user/profile/addCountry?countryCode=DE" -H "X-USER-ID: af5ea882-1008-480c-a842-5e44a9127e43"

# removeUserCountry Endpoint
curl -X POST "http://localhost:8086/api/user/v0/user/profile/removeCountry?countryCode=DE" -H "X-USER-ID: af5ea882-1008-480c-a842-5e44a9127e43"

# addUserLanguage Endpoint
curl -X POST "http://localhost:8086/api/user/v0/user/profile/addLanguage?languageCode=de" -H "X-USER-ID: af5ea882-1008-480c-a842-5e44a9127e43"

# removeUserLanguage Endpoint
curl -X POST "http://localhost:8086/api/user/v0/user/profile/removeLanguage?languageCode=de" -H "X-USER-ID: af5ea882-1008-480c-a842-5e44a9127e43"