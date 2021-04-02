import { UNDEFINED_ERROR_MESSAGE } from '../constants';

export default async function fetchResource({method, url, headers, requestBody, messageMapping}) {
  const options = {
    method,
    headers: {...headers},
  };
  if (requestBody) {
    options.body = JSON.stringify(requestBody);
    options.headers['Content-Type'] = 'application/json';
  }

  try {
    const response = await fetch(url, options);
    if (response.status != 200) {
      return {
        status: response.status,
        error: messageMapping[response.status] || UNDEFINED_ERROR_MESSAGE,
      };
    }
    let body;
    const contentType = response.headers.get('Content-Type');
    if (contentType &&  contentType === 'application/json') {
      body = await response.json();
    }
    return { status: response.status, ...(body && { body }) };
  } catch(error) {
    return {
      status: 500,
      error: UNDEFINED_ERROR_MESSAGE,
    };
  }
}