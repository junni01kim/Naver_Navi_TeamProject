import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.Utmk
import kotlin.math.abs
import kotlin.math.sqrt

fun main(){
    // 점과 직선 사이의 거리 - 545동 ~ 도로변 : 15.2 예상
    val from = LatLng(37.641809,126.834994)
    val to = LatLng(37.641477, 126.834928)
    val user = LatLng(37.641695,126.834783)

    var nfrom = Utmk.valueOf(from)
    var nto = Utmk.valueOf(to)
    var nuser = Utmk.valueOf(user)

    // 561동 ~ 도로변 : 20예상
    val from1 = LatLng(37.641673,126.833863)
    val to1 = LatLng(37.641584,126.834543)
    val user1 = LatLng(37.641448,126.834209)

    var nfrom1 = Utmk.valueOf(from1)
    var nto1 = Utmk.valueOf(to1)
    var nuser1 = Utmk.valueOf(user1)

    //충장로 도로 너비 - 30.7 예상 - Fault
    val from2 = LatLng(37.642140,126.838811)
    val to2 = LatLng(37.642025,126.838795)
    val user2 = LatLng(37.642110,126.838639)

    var nfrom2 = Utmk.valueOf(from2)
    var nto2 = Utmk.valueOf(to2)
    var nuser2 = Utmk.valueOf(user2)

    //화수 공원 ~ 김포시
    val from3 = LatLng(37.644922,126.838845)
    val to3 = LatLng(37.643953,126.838754)
    val user3 = LatLng(37.647809,126.602274)

    var nfrom3 = Utmk.valueOf(from3)
    var nto3 = Utmk.valueOf(to3)
    var nuser3 = Utmk.valueOf(user3)

    // 충장로 re - Fault
    val from4 = LatLng(37.646843,126.839333)
    val to4 = LatLng(37.646376,126.839285)
    val user4 = LatLng(37.646563,126.839124)

    var nfrom4 = Utmk.valueOf(from4)
    var nto4 = Utmk.valueOf(to4)
    var nuser4 = Utmk.valueOf(user4)

    // 오차 10m 이내 - 7m예상 - Fault
    val from5 = LatLng(37.642660,126.835547)
    val to5 = LatLng(37.642630,126.835821)
    val user5 = LatLng(37.642519,126.835633)

    var nfrom5 = Utmk.valueOf(from5)
    var nto5 = Utmk.valueOf(to5)
    var nuser5 = Utmk.valueOf(user5)

    //LJH
    //37.5665, 126.9780, 37.5665, 126.9781, 37.5665 + 13/111000, 126.97805
    val from6 = LatLng(37.5665, 126.9780)
    val to6 = LatLng(37.5665, 126.9781)
    val user6 = LatLng(37.5665 + 13.000/111000, 126.97805)

    var nfrom6 = Utmk.valueOf(from6)
    var nto6 = Utmk.valueOf(to6)
    var nuser6 = Utmk.valueOf(user6)


    println("점과 직선 사이의 거리 : " + calcSpotLine(nfrom,nto,nuser))
    println("점과 직선 사이의 거리 : " + calcSpotLine(nfrom1,nto1,nuser1))
    println("점과 직선 사이의 거리 : " + calcSpotLine(nfrom2,nto2,nuser2))
    println("점과 직선 사이의 거리 : " + calcSpotLine(nfrom3,nto3,nuser3))
    println("점과 직선 사이의 거리 : " + calcSpotLine(nfrom4,nto4,nuser4))
    println("점과 직선 사이의 거리 : " + calcSpotLine(nfrom5,nto5,nuser5))
    println("점과 직선 사이의 거리(LJH) : " + calcSpotLine(nfrom6,nto6,nuser6))

}

fun calcSpotLine(from:Utmk, to:Utmk, user:Utmk):Double{
    var res = 0.0

    //기울기
    var slope = (from.y - to.y)/(from.x - to.x)

    // y절편
    var yCoeff = from.y - slope*from.x

    var a = -1*slope
    var b = 1
    var c = -1*yCoeff

    res = abs(a*(user.x) + b*(user.y) + c) / sqrt(a*a + b*b)

    return res
}