import axios from "axios"
import "./interceptor"

const BASE_URL = "/api/recruitments"

export const fetchItems = recruitmentId => {
    return axios.get(`${BASE_URL}/${recruitmentId}/items`)
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
                        id: 5,
                        title: "웹 프론트엔드 3기",
                        startTime: new Date("2020-09-24T15:00:00"),
                        endTime: new Date("2020-11-09T23:59:00"),
                        recruitmentStatus: "RECRUITING",
                    },
                    {
                        id: 4,
                        title: "모바일(iOS) 3기",
                        startTime: new Date("2020-10-27T15:00:00"),
                        endTime: new Date("2020-11-09T23:59:00"),
                        recruitmentStatus: "RECRUITABLE",
                    },
                    {
                        id: 3,
                        title: "모바일(Android) 3기",
                        startTime: new Date("2020-10-24T15:00:00"),
                        endTime: new Date("2020-11-09T23:59:00"),
                        recruitmentStatus: "UNRECRUITABLE",
                    },
                    {
                        id: 2,
                        title: "웹 백엔드 2기",
                        startTime: new Date("2019-10-24T15:00:00"),
                        endTime: new Date("2019-11-09T23:59:00"),
                        recruitmentStatus: "ENDED",
                    },
                ],
            }),
        )
}
