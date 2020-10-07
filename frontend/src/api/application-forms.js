import axios from "axios"
import {convert} from "@/api/converter";

const BASE_URL = "/api/application-forms"

export const fetchMyApplicationForms = token => {
    return convert(axios
        .get(`${BASE_URL}/me`, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        }).data)
        .catch(() =>
            Promise.resolve({
                data: [
                    {
                        id: 5,
                        referenceUrl: "https://www.google.com",
                        submitted: false,
                        recruitmentId: 5,
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
                    },
                    {
                        id: 4,
                        referenceUrl: "https://www.google.com",
                        submitted: false,
                        recruitmentId: 4,
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
                    },
                    {
                        id: 3,
                        referenceUrl: "https://www.google.com",
                        submitted: true,
                        recruitmentId: 3,
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
                    },
                    {
                        id: 2,
                        referenceUrl: "https://www.google.com",
                        submitted: true,
                        recruitmentId: 2,
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
                    },
                ],
            }),
        )
}

export const fetchForm = ({token, recruitmentId}) =>
    convert(axios.get(`${BASE_URL}`, {
        headers: {
            Authorization: `Bearer ${token}`,
        },
        params: {
            recruitmentId,
        },
    }).data)
        .catch(() =>
            Promise.resolve({
                data: {
                    referenceUrl: "https://www.google.com",
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
                },
            }),
        )
