import service from "../utils/request";

export function FindAllCabinet() {
    return service({
        url: '/cabinet/list',
        method: 'get'
    })
}

export function SaveCabinet(data) {
    return service({
        url: '/cabinet/save',
        method: 'post',
        data: data
    })
}

export function deleteCabinet(id) {
    return service({
        url: '/cabinet?id=' + id,
        method: 'delete'
    })
}

export function UpdateCabinet(data) {
    return service({
        url: '/cabinet/update',
        method: 'post',
        data: data
    })
}
