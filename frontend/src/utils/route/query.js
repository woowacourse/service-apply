const parseQuery = (query) => {
  const queryObj = {};

  query
    .slice(1)
    .split("&")
    .forEach((item) => {
      const [key, value] = item.split("=");

      queryObj[key] = value;
    });

  return queryObj;
};

export default parseQuery;
