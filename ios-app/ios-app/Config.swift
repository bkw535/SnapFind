import Foundation

enum Config {
    enum Error: Swift.Error {
        case missingKey, invalidValue
    }

    static func value<T>(for key: String) throws -> T where T: LosslessStringConvertible {
        guard let object = Bundle.main.object(forInfoDictionaryKey: key) else {
            throw Error.missingKey
        }

        switch object {
        case let value as T:
            return value
        case let string as String:
            guard let value = T(string) else { fallthrough }
            return value
        default:
            throw Error.invalidValue
        }
    }

    static let googleClientId = "1047662398012-cq3e6ih1906jtkjj0pt88u66bc5292ov.apps.googleusercontent.com"
    static let googleRedirectUri = "snapfind://oauth2/callback"
    static let apiBaseUrl = "https://snapfind.p-e.kr"
}

enum Configuration {
    static var googleClientId: String {
        return try! Config.value(for: "GOOGLE_CLIENT_ID")
    }
    
    static var googleRedirectUri: String {
        return try! Config.value(for: "GOOGLE_REDIRECT_URI")
    }
    
    static var apiBaseUrl: String {
        return try! Config.value(for: "API_BASE_URL")
    }
} 