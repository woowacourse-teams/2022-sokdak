import fetcher from './fetcher';
import extractDataFromAxios from './util/extractor';

export interface GetHashTagsResponse {
  hashtags: Hashtag[];
}

export const requestGetHashTags = async (limit: number, include: string) =>
  extractDataFromAxios<GetHashTagsResponse>(fetcher.get(`/hashtags/popular?limit=${limit}&include=${include}`));
