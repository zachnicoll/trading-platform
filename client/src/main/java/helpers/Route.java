package helpers;

/**
 * All available API routes/endpoints. Use getRoute to wrap the routes in forward-slashes to format
 * them for requests. E.g. "/assets/".
 */
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
