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
curl -X POST "http://localhost:8086/api/user/v0/user/profile/add-country?countryCode=DE" -H "X-USER-ID: af5ea882-1008-480c-a842-5e44a9127e43"

# removeUserCountry Endpoint
curl -X POST "http://localhost:8086/api/user/v0/user/profile/remove-country?countryCode=DE" -H "X-USER-ID: af5ea882-1008-480c-a842-5e44a9127e43"

# addUserLanguage Endpoint
curl -X POST "http://localhost:8086/api/user/v0/user/profile/add-language?languageCode=de" -H "X-USER-ID: af5ea882-1008-480c-a842-5e44a9127e43"

# removeUserLanguage Endpoint
curl -X POST "http://localhost:8086/api/user/v0/user/profile/remove-language?languageCode=de" -H "X-USER-ID: af5ea882-1008-480c-a842-5e44a9127e43"