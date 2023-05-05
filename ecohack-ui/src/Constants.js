const prod = {
  url: {
    API_BASE_URL: 'https://backend-organizers.lhindtia-devenv.com',
    OAUTH2_REDIRECT_URI: 'https://backend-organizers.lhindtia-devenv.com'
  }
}

const dev = {
  url: {
    API_BASE_URL: 'http://localhost:8080'
  }
}

export const config = process.env.NODE_ENV === 'development' ? dev : prod