export const fetchForm = ({ token, recruitmentId }) =>
  new Promise((resolve, reject) => {
    if (token === "") {
      return reject()
    }
    if (recruitmentId !== 1) {
      return reject()
    }
    resolve({
      data: {
        referenceUrl: "https://www.google.com",
        submitted: true,
        createdDateTime: "2019-10-25T10:00:00",
        modifiedDateTime: "2019-11-05T10:00:00",
        submittedDateTime: "2019-11-05T10:00:00",
        recruitmentId: 1,
        applicantId: 2,
        answers: {
          items: [
            {
              contents: "스타트업을 하고 싶습니다.",
              recruitmentItemId: 1,
            },
            {
              contents: "책임감",
              recruitmentItemId: 2,
            },
          ],
        },
        id: 2,
      },
    })
  })
