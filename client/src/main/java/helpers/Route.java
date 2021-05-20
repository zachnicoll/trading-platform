package helpers;

public enum Route {
    assets,
    assettype,
    login,
    orgunit,
    refresh,
    resetpassword,
    trades,
    user;

    public static String getRoute(Route route){
        return String.format("/%s/", route);
    }
}
