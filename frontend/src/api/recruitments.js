import axios from "axios"

const BASE_URL = "/api/recruitments"

export const fetchItems = recruitmentId => {
  return axios.get(`${BASE_URL}/${recruitmentId}/items`).catch(() =>
    Promise.resolve({
      data: [
        {
          id: 1,
          title: "프로그래밍 학습 과정과 현재 자신이 생각하는 역량은?",
          description:
            "우아한테크코스는 프로그래밍에 대한 기본 지식과 경험을 가진 교육생을 선발하기 때문에 프로그래밍 경험이 있는 상태에서 지원하게 됩니다. 프로그래밍 학습을 어떤 계기로 시작했으며, 어떻게 학습해왔는지, 이를 통해 현재 어느 정도의 역량을 보유한 상태인지를 구체적으로 작성해 주세요.",
          maximumLength: 1000,
        },
        {
          id: 2,
          title: "프로그래머가 되려는 이유는 무엇인가요?",
          description:
            "어떤 계기로 프로그래머라는 직업을 꿈꾸게 되었나요? 프로그래밍을 배워 최종적으로 하고 싶은 일이 무엇인지, 프로그래밍을 통해 만들고 싶은 소프트웨어가 있다면 무엇인지에 대해 작성해 주세요.",
          maximumLength: 1000,
        },
      ],
    }),
  )
}

export const fetchMyRecruitments = token => {
  return axios
    .get(`${BASE_URL}/me`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
    .catch(() =>
      Promise.resolve({
        data: [
          {
            id: 1,
            title: "웹 프론트엔드 3기",
            startTime: new Date("2020-09-24T15:00:00"),
            endTime: new Date("2020-11-09T23:59:00"),
            recruitmentStatus: "RECRUITING",
          },
          {
            id: 2,
            title: "웹 백엔드 3기",
            startTime: new Date("2020-09-24T15:00:00"),
            endTime: new Date("2020-11-09T23:59:00"),
            recruitmentStatus: "RECRUITING",
          },
        ],
      }),
    )
}
