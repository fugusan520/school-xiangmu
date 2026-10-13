import service from "../utils/request";

export function findBoxList(cabinetId) {
    return service({
        url: '/cabinet/box/list?cabinetId=' + cabinetId,
        method: 'get'
    })
}

export function saveBox(data) {
    return service({
        url: '/cabinet/box/save',
        method: 'post',
        data: data
    })
}

export function deleteBox(id) {
    return service({
        url: '/cabinet/box/delete?id=' + id,
        method: 'delete'
    })
}

export function updateBoxStatus(data) {
    return service({
        url: '/cabinet/box/update',
        method: 'post',
        data: data
    })
}


export function batchSave(data) {
    return service({
        url: '/cabinet/box/batchSave',
        method: 'post',
        data: data
    })
}

export function findBoxList2() {
    return service({
        url: '/cabinet/box/list?cabinetId=',
        method: 'get'
    })
}

export function findCanUseBoxList() {
    return service({
        url: '/cabinet/box/canUseList',
        method: 'get'
    })
}