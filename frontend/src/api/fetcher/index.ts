import axios from 'axios';

const fetcher = axios.create({
  baseURL: process.env.API_URL!,
  withCredentials: true,
});

export default fetcher;
