export const parseQuery = (query: URL["search"]) => {
  const queryObj: Record<string, string> = {};

  query
    .slice(1)
    .split("&")
    .forEach((item) => {
      const [key, value] = item.split("=");

      queryObj[key] = value;
    });

  return queryObj;
};

export const generateQuery = (queryObj: Record<string, string>) => {
  const queryKeys = Object.keys(queryObj);

  return `?${queryKeys.map((key) => `${key}=${queryObj[key]}`).join("&")}`;
};
