class FingerModel {
  final String taskId;
  final String key;
  final String baseUrl;
  final String token;

  FingerModel({
    required this.taskId,
    required this.key,
    required this.baseUrl,
    required this.token,
  });

  factory FingerModel.fromJson(Map<String, dynamic> json) {
    return FingerModel(
      taskId: json['task_id'],
      key: json['key'],
      baseUrl: json['base_url'],
      token: json['token'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'task_id': taskId,
      'key': key,
      'base_url': baseUrl,
      'token': token,
    };
  }
}
